package utils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class EmailSender {

    /**
     * ExtentReport HTML faylını email ilə göndərir
     *
     * @param reportPath - Report faylının yolu
     */
    public static void sendReport(String reportPath) {
        // ✅ Email Konfiqurasiyası
        final String fromEmail = "nurlan.azstudy@gmail.com";
        final String password = "ipwqvnbyzimfxksc";
        final String toEmail = "nurlanbagishli@gmail.com";

        // ✅ Tarix formatı
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
        String currentDateTime = LocalDateTime.now().format(dateFormatter);
        String fileDateTime = LocalDateTime.now().format(fileFormatter);

        // ✅ Report faylını yoxla
        File reportFile = new File(reportPath);
        if (!reportFile.exists()) {
            System.err.println("\n❌ XƏTA: Report faylı tapılmadı!");
            System.err.println("═══════════════════════════════════════════════════");
            System.err.println("Axtarılan yol: " + reportPath);
            System.err.println("Mövcud qovluq: " + new File(".").getAbsolutePath());
            System.err.println("═══════════════════════════════════════════════════");

            // Alternativ yolları yoxla
            String[] alternativePaths = {
                    "ExtentReport.html",
                    "target/ExtentReport.html",
                    "test-output/ExtentReport.html",
                    "reports/ExtentReport.html"
            };

            System.err.println("\nAlternativ yollar yoxlanır...");
            for (String altPath : alternativePaths) {
                File altFile = new File(altPath);
                if (altFile.exists()) {
                    System.out.println("✅ Tapıldı: " + altFile.getAbsolutePath());
                    reportFile = altFile;
                    reportPath = altPath;
                    break;
                } else {
                    System.err.println("❌ Yoxdur: " + altPath);
                }
            }

            if (!reportFile.exists()) {
                System.err.println("\n❌ Heç bir report fayl tapılmadı! Email göndərilmədi.\n");
                return;
            }
        }

        System.out.println("\n✅ Report faylı tapıldı: " + reportFile.getAbsolutePath());
        System.out.println("   Ölçü: " + (reportFile.length() / 1024) + " KB\n");

        // ✅ Gmail SMTP Ayarları
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // ✅ Autentifikasiya
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // ✅ Email Mesajı Yarat
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("🎓 StudyLeo Test Report - " + currentDateTime);

            // ✅ Email Body (HTML)
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlContent =
                    "<div style='font-family: Arial, sans-serif; padding: 20px; background: #f5f5f5;'>" +
                            "<div style='background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>" +
                            "<h2 style='color: #2c3e50; border-bottom: 3px solid #3498db; padding-bottom: 10px;'>🎉 StudyLeo Test Automation Report</h2>" +
                            "<table style='width: 100%; margin-top: 20px; border-collapse: collapse;'>" +
                            "<tr style='background: #ecf0f1;'>" +
                            "<td style='padding: 12px; font-weight: bold; width: 150px;'>📅 Test Tarixi:</td>" +
                            "<td style='padding: 12px;'>" + currentDateTime + "</td>" +
                            "</tr>" +
                            "<tr>" +
                            "<td style='padding: 12px; font-weight: bold;'>👨‍💻 Tester:</td>" +
                            "<td style='padding: 12px;'>Nurlan Bağışlı (nurlanbagishli)</td>" +
                            "</tr>" +
                            "<tr style='background: #ecf0f1;'>" +
                            "<td style='padding: 12px; font-weight: bold;'>📊 Layihə:</td>" +
                            "<td style='padding: 12px;'>StudyLeo - Universitet Button Testləri</td>" +
                            "</tr>" +
                            "<tr>" +
                            "<td style='padding: 12px; font-weight: bold;'>📁 Report Faylı:</td>" +
                            "<td style='padding: 12px; color: #2ecc71; font-weight: bold;'>StudyLeo-Test-Report-" + fileDateTime + ".html</td>" +
                            "</tr>" +
                            "<tr style='background: #ecf0f1;'>" +
                            "<td style='padding: 12px; font-weight: bold;'>📦 Fayl Ölçüsü:</td>" +
                            "<td style='padding: 12px;'>" + (reportFile.length() / 1024) + " KB</td>" +
                            "</tr>" +
                            "</table>" +
                            "<div style='margin-top: 30px; padding: 20px; background: #e8f5e9; border-left: 4px solid #4caf50; border-radius: 5px;'>" +
                            "<p style='margin: 0; color: #2e7d32; font-size: 16px;'>✅ Test nəticələri əlavə fayla əlavə edilmişdir.</p>" +
                            "<p style='margin: 10px 0 0 0; color: #666; font-size: 14px;'>Ətraflı məlumat üçün HTML report faylını açın.</p>" +
                            "</div>" +
                            "<div style='margin-top: 30px; padding: 15px; background: #fff3cd; border-left: 4px solid #ffc107; border-radius: 5px;'>" +
                            "<p style='margin: 0; color: #856404; font-size: 14px;'>📌 <strong>Qeyd:</strong> Report faylını browser-də açaraq tam nəticələri görə bilərsiniz.</p>" +
                            "</div>" +
                            "</div>" +
                            "</div>";

            messageBodyPart.setContent(htmlContent, "text/html; charset=utf-8");

            // ✅ Report Faylını Əlavə Et (Attachment)
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(reportFile);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName("StudyLeo-Test-Report-" + fileDateTime + ".html");
            multipart.addBodyPart(attachmentPart);

            // ✅ Emaili Göndər
            message.setContent(multipart);
            Transport.send(message);

            System.out.println("═══════════════════════════════════════════════════");
            System.out.println("✅ EMAIL UĞURLA GÖNDƏRİLDİ!");
            System.out.println("═══════════════════════════════════════════════════");
            System.out.println("   📧 Göndərən: " + fromEmail);
            System.out.println("   📨 Alan:     " + toEmail);
            System.out.println("   📅 Tarix:    " + currentDateTime);
            System.out.println("   📎 Fayl:     StudyLeo-Test-Report-" + fileDateTime + ".html");
            System.out.println("   📊 Ölçü:     " + (reportFile.length() / 1024) + " KB");
            System.out.println("═══════════════════════════════════════════════════\n");

        } catch (MessagingException e) {
            System.err.println("\n❌ EMAIL GÖNDƏRİLMƏ XƏTASI!");
            System.err.println("═══════════════════════════════════════════════════");
            System.err.println("Xəta: " + e.getMessage());
            System.err.println("═══════════════════════════════════════════════════\n");
            e.printStackTrace();
        }
    }

    /**
     * Çoxlu alıcılara göndərmək
     */
    public static void sendReportToMultiple(String reportPath, String[] recipients) {
        final String fromEmail = "nurlan.azstudy@gmail.com";
        final String password = "ipwqvnbyzimfxksc";

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
        String currentDateTime = LocalDateTime.now().format(dateFormatter);
        String fileDateTime = LocalDateTime.now().format(fileFormatter);

        File reportFile = new File(reportPath);
        if (!reportFile.exists()) {
            System.err.println("❌ Report faylı tapılmadı: " + reportPath);
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));

            InternetAddress[] addresses = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                addresses[i] = new InternetAddress(recipients[i]);
            }
            message.setRecipients(Message.RecipientType.TO, addresses);

            message.setSubject("🎓 StudyLeo Test Report - " + currentDateTime);

            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlContent =
                    "<div style='font-family: Arial, sans-serif; padding: 20px; background: #f5f5f5;'>" +
                            "<div style='background: white; padding: 30px; border-radius: 10px;'>" +
                            "<h2 style='color: #2c3e50;'>🎉 StudyLeo Test Report</h2>" +
                            "<p><strong>Test Date:</strong> " + currentDateTime + "</p>" +
                            "<p><strong>Tester:</strong> Nurlan Bağışlı</p>" +
                            "<p>Test results attached.</p>" +
                            "</div>" +
                            "</div>";

            messageBodyPart.setContent(htmlContent, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(reportFile);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
            Transport.send(message);

            System.out.println("\n✅ Email göndərildi: " + String.join(", ", recipients) + "\n");

        } catch (Exception e) {
            System.err.println("\n❌ Email xətası: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
}