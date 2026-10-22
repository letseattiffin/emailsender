package com.sagar.recruitermail.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class ExcelReaderService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Value("${app.mail.recipients-file}")
    private String recipientsFile;

    public List<String> readRecruiterEmails() {
        Set<String> emails = new LinkedHashSet<>();

        try {
            ClassPathResource resource = new ClassPathResource(recipientsFile);

            try (InputStream inputStream = resource.getInputStream();
                 Workbook workbook = WorkbookFactory.create(inputStream)) {

                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue; // header row
                    }

                    Cell cell = row.getCell(0);
                    if (cell == null) {
                        continue;
                    }

                    String email = cell.toString().trim().toLowerCase();

                    if (!email.isBlank() && EMAIL_PATTERN.matcher(email).matches()) {
                        emails.add(email);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to read recruiter Excel file: " + recipientsFile, e);
        }

        return new ArrayList<>(emails);
    }
}
