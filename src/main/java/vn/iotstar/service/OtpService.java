package vn.iotstar.service;

public interface OtpService {
    enum IssueResult { SENT, THROTTLED, MAIL_FAILED }

    enum VerifyResult { OK, INVALID, EXPIRED, TOO_MANY_ATTEMPTS }

    /** Generates a new 6-digit OTP, stores its HMAC and emails the code. */
    IssueResult issue(String email, String purpose);

    VerifyResult verify(String email, String purpose, String code);
}
