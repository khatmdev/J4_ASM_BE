package org.example.java4_asm_backend.utils;

import jakarta.mail.Authenticator;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mailer {
    private static final ExecutorService executor = Executors.newFixedThreadPool(5); // Thread pool size

    private static final String ADMIN_EMAIL = "khatmps40141@gmail.com"; // Email mặc định
    private static final String ADMIN_PASS_APP = "xkxd lupa ojka qtan"; // App Password

    public static boolean send(String fromEmail, String appPassword, String to, String subject, String body) {
        // Thông số kết nối GMail
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "587");

        // Đăng nhập GMail với App Password của người dùng
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword); // Sử dụng App Password của người dùng
            }
        });

        try {
            // Tạo mail
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(fromEmail));
            mail.setRecipients(RecipientType.TO, to);
            mail.setSubject(subject, "utf-8");
            mail.setText(body, "utf-8", "html");
            mail.setReplyTo(mail.getFrom());

            // Gửi mail
            Transport.send(mail);
            return true;
        } catch (Exception e) {
            System.out.println("Error sending email: " + e);
            return false;
        }
    }

    public static void sendEmailsAsync(String fromEmail, String appPassword, List<String> emails, String subject, String body) {
        executor.submit(() -> {
            for (String email : emails) {
                boolean success = Mailer.send(fromEmail, appPassword, email, subject, body);
                if (!success) {
                    System.out.println("Failed to send email to: " + email);
                }
            }
        });
    }

    public static boolean send(String to, String subject, String body) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ADMIN_EMAIL, ADMIN_PASS_APP);
            }
        });

        try {
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(ADMIN_EMAIL));
            mail.setRecipients(RecipientType.TO, to);
            mail.setSubject(subject, "utf-8");
            mail.setText(body, "utf-8", "html");
            mail.setReplyTo(mail.getFrom());

            Transport.send(mail);
            return true;
        } catch (Exception e) {
            System.out.println("Error sending email: " + e);
            return false;
        }
    }
}
