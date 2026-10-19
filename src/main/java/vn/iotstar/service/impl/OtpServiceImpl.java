package vn.iotstar.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import vn.iotstar.dao.OtpDao;
import vn.iotstar.dao.impl.OtpDaoImpl;
import vn.iotstar.model.Otp;
import vn.iotstar.service.OtpService;
import vn.iotstar.util.AppConfig;
import vn.iotstar.util.Constant;
import vn.iotstar.util.MailUtil;

public class OtpServiceImpl implements OtpService {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final OtpDao otpDao = new OtpDaoImpl();

    @Override
    public IssueResult issue(String email, String purpose) {
        long now = System.currentTimeMillis();
        long cooldownMs = AppConfig.getInt("otp.resend.seconds", 60) * 1000L;
        Otp existing = otpDao.get(email, purpose);
        if (existing != null && now - existing.getCreatedAt().getTime() < cooldownMs) {
            return IssueResult.THROTTLED;
        }

        String code = String.format("%06d", RANDOM.nextInt(1_000_000));
        int minutes = AppConfig.getInt("otp.minutes", 10);
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setPurpose(purpose);
        otp.setCodeHash(hash(email, purpose, code));
        otp.setCreatedAt(new Timestamp(now));
        otp.setExpiresAt(new Timestamp(now + minutes * 60_000L));
        otpDao.save(otp);

        String what = Constant.OTP_ACTIVATE.equals(purpose) ? "kích hoạt tài khoản" : "đặt lại mật khẩu";
        String body = "Mã OTP " + what + " của bạn là: " + code + "\n\n"
                + "Mã có hiệu lực trong " + minutes + " phút. Không chia sẻ mã này với bất kỳ ai.\n"
                + "Nếu bạn không thực hiện yêu cầu này, hãy bỏ qua email.";
        try {
            MailUtil.send(email, "Mã OTP " + what, body);
            return IssueResult.SENT;
        } catch (Exception e) {
            e.printStackTrace();
            return IssueResult.MAIL_FAILED;
        }
    }

    @Override
    public VerifyResult verify(String email, String purpose, String code) {
        Otp otp = otpDao.get(email, purpose);
        if (otp == null || otp.getExpiresAt().getTime() < System.currentTimeMillis()) {
            if (otp != null) {
                otpDao.delete(email, purpose);
            }
            return VerifyResult.EXPIRED;
        }
        if (otp.getAttempts() >= AppConfig.getInt("otp.max.attempts", 5)) {
            otpDao.delete(email, purpose);
            return VerifyResult.TOO_MANY_ATTEMPTS;
        }
        byte[] expected = otp.getCodeHash().getBytes(StandardCharsets.UTF_8);
        byte[] actual = hash(email, purpose, code == null ? "" : code.trim()).getBytes(StandardCharsets.UTF_8);
        if (MessageDigest.isEqual(expected, actual)) {
            otpDao.delete(email, purpose); // one-time use
            return VerifyResult.OK;
        }
        otpDao.increaseAttempts(email, purpose);
        return VerifyResult.INVALID;
    }

    private static String hash(String email, String purpose, String code) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(AppConfig.get("remember.secret", "change-me").getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"));
            byte[] out = mac.doFinal((email + "|" + purpose + "|" + code).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : out) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
