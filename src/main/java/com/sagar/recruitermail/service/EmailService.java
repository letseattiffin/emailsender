package com.sagar.recruitermail.service;

import com.sagar.recruitermail.model.EmailSendResult;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final ExcelReaderService excelReaderService;
    private final EmailTemplateService templateService;

    @Value("${app.mail.from}")
    private String from;

    @Value("${app.mail.subject}")
    private String subject;

    @Value("${app.mail.cv-file}")
    private String cvFile;

    @Value("${app.mail.delay-ms:3000}")
    private long delayMs;

    @Value("${app.mail.max-recipients:100}")
    private int maxRecipients;

    @Value("${app.mail.sending-enabled:false}")
    private boolean sendingEnabled;

    public EmailService(JavaMailSender mailSender,
                        ExcelReaderService excelReaderService,
                        EmailTemplateService templateService) {
        this.mailSender = mailSender;
        this.excelReaderService = excelReaderService;
        this.templateService = templateService;
    }

    public EmailSendResult sendToAllRecruiters() {

        if (!sendingEnabled) {
            throw new IllegalStateException(
                    "Email sending is disabled. Set MAIL_SENDING_ENABLED=true before sending."
            );
        }

        List<String> recipients = excelReaderService.readRecruiterEmails();

        if (recipients.isEmpty()) {
            throw new IllegalStateException("No valid recruiter email IDs found.");
        }

        if (recipients.size() > maxRecipients) {
            throw new IllegalStateException(
                    "Recipient count " + recipients.size()
                            + " exceeds configured maximum of " + maxRecipients + "."
            );
        }

        String htmlTemplate = templateService.loadTemplate();

        List<String> successful = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        for (String recipient : recipients) {
            try {
                sendSingleEmail(recipient, htmlTemplate);
                successful.add(recipient);
                System.out.println("SENT -> " + recipient);

                if (delayMs > 0) {
                    Thread.sleep(delayMs);
                }

            } catch (Exception e) {
                failed.add(recipient);
                System.err.println("FAILED -> " + recipient + " : " + e.getMessage());
            }
        }

        return new EmailSendResult(
                recipients.size(),
                successful.size(),
                failed.size(),
                successful,
                failed
        );
    }

    private void sendSingleEmail(String recipient, String htmlBody) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

        helper.setFrom(from);
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        ClassPathResource cvResource = new ClassPathResource(cvFile);
        helper.addAttachment("Sagar_Auti_CV.pdf", cvResource);

        mailSender.send(message);
    }
}
