package vn.iotstar.util;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/** Sends plain-text emails through the SMTP server configured in app.properties (mail.*). */
public final class MailUtil {
    private MailUtil() {
    }

    public static void send(String to, String subject, String body) throws MessagingException {
        final String user = AppConfig.get("mail.username", "");
        final String pass = AppConfig.get("mail.password", "");
        if (user.isEmpty() || pass.isEmpty()) {
            throw new MessagingException("SMTP chưa được cấu hình (mail.username / mail.password)");
        }
        String from = AppConfig.get("mail.from", user);

        Properties p = new Properties();
        p.put("mail.smtp.host", AppConfig.get("mail.host", "smtp.gmail.com"));
        p.put("mail.smtp.port", AppConfig.get("mail.port", "587"));
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", AppConfig.get("mail.starttls", "true"));
        p.put("mail.smtp.connectiontimeout", "10000");
        p.put("mail.smtp.timeout", "10000");
        p.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        Transport.send(msg);
    }
}
