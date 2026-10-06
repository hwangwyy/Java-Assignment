package vn.iotstar.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * "Remember me" cookie. The cookie holds  base64(username)|expiryMillis|HMAC  so it cannot be
 * forged without knowing remember.secret (storing the bare username would let anyone log in as anyone).
 */
public final class RememberMeUtil {
    private static final String SECRET = AppConfig.get("remember.secret", "change-me");
    private static final int DAYS = AppConfig.getInt("remember.days", 7);

    private RememberMeUtil() {
    }

    public static void save(HttpServletRequest req, HttpServletResponse resp, String username) {
        long expiry = System.currentTimeMillis() + DAYS * 24L * 3600_000L;
        String payload = b64(username) + "|" + expiry;
        Cookie c = new Cookie(Constant.COOKIE_REMEMBER, payload + "|" + sign(payload));
        c.setMaxAge(DAYS * 24 * 3600);
        c.setHttpOnly(true);
        c.setPath(contextPath(req));
        resp.addCookie(c);
    }

    public static void clear(HttpServletRequest req, HttpServletResponse resp) {
        Cookie c = new Cookie(Constant.COOKIE_REMEMBER, "");
        c.setMaxAge(0);
        c.setHttpOnly(true);
        c.setPath(contextPath(req));
        resp.addCookie(c);
    }

    /** @return the username in a valid, unexpired remember-me cookie, or null. */
    public static String read(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie c : cookies) {
            if (!Constant.COOKIE_REMEMBER.equals(c.getName())) {
                continue;
            }
            String[] p = c.getValue().split("\\|");
            if (p.length != 3) {
                return null;
            }
            String payload = p[0] + "|" + p[1];
            boolean ok = MessageDigest.isEqual(sign(payload).getBytes(StandardCharsets.UTF_8),
                    p[2].getBytes(StandardCharsets.UTF_8));
            try {
                if (ok && Long.parseLong(p[1]) > System.currentTimeMillis()) {
                    return new String(Base64.getUrlDecoder().decode(p[0]), StandardCharsets.UTF_8);
                }
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    private static String contextPath(HttpServletRequest req) {
        String cp = req.getContextPath();
        return (cp == null || cp.isEmpty()) ? "/" : cp;
    }

    private static String b64(String s) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    private static String sign(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
