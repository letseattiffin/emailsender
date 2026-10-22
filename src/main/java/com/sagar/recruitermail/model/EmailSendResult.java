package com.sagar.recruitermail.model;

import java.util.List;

public record EmailSendResult(
        int totalRecipients,
        int successCount,
        int failedCount,
        List<String> successfulEmails,
        List<String> failedEmails
) {
}
