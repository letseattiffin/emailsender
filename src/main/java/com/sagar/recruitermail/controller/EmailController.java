package com.sagar.recruitermail.controller;

import com.sagar.recruitermail.model.EmailSendResult;
import com.sagar.recruitermail.service.EmailService;
import com.sagar.recruitermail.service.ExcelReaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final EmailService emailService;
    private final ExcelReaderService excelReaderService;

    public EmailController(EmailService emailService,
                           ExcelReaderService excelReaderService) {
        this.emailService = emailService;
        this.excelReaderService = excelReaderService;
    }

    @GetMapping("/recipients")
    public ResponseEntity<Map<String, Object>> getRecipients() {
        List<String> emails = excelReaderService.readRecruiterEmails();

        return ResponseEntity.ok(Map.of(
                "count", emails.size(),
                "emails", emails
        ));
    }

    @PostMapping("/send-all")
    public ResponseEntity<EmailSendResult> sendAll() {
        return ResponseEntity.ok(emailService.sendToAllRecruiters());
    }
}
