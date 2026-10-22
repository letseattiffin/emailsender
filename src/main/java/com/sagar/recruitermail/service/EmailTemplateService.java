package com.sagar.recruitermail.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailTemplateService {

    @Value("${app.mail.template-file}")
    private String templateFile;

    public String loadTemplate() {
        try {
            ClassPathResource resource = new ClassPathResource(templateFile);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load email template: " + templateFile, e);
        }
    }
}
