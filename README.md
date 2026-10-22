# Recruiter Email Sender

Simple Spring Boot project that:

- Reads recruiter email IDs from `email_id_list.xlsx`
- Sends one email to each recruiter
- Attaches `Sagar_Auti_CV.pdf`
- Uses an HTML email template
- Avoids hardcoding the Gmail app password
- Adds a delay between emails
- Deduplicates recipient addresses
- Limits the maximum number of recipients

## 1. Requirements

- Java 17+
- Maven
- Gmail account with SMTP access
- Gmail App Password if required by your Google account configuration

## 2. Set environment variables

### Windows PowerShell

```powershell
$env:MAIL_USERNAME="sagarauti302@gmail.com"
$env:MAIL_APP_PASSWORD="YOUR_APP_PASSWORD"
$env:MAIL_SENDING_ENABLED="true"
```

### Windows CMD

```cmd
set MAIL_USERNAME=sagarauti302@gmail.com
set MAIL_APP_PASSWORD=YOUR_APP_PASSWORD
set MAIL_SENDING_ENABLED=true
```

Do not put your real password or app password inside `application.properties`.

## 3. Run the project

```bash
mvn spring-boot:run
```

## 4. Check recipients before sending

Open:

```text
GET http://localhost:8080/api/emails/recipients
```

Current Excel format:

```text
email_id
recruiter1@example.com
recruiter2@example.com
```

## 5. Send emails

```text
POST http://localhost:8080/api/emails/send-all
```

Example response:

```json
{
  "totalRecipients": 2,
  "successCount": 2,
  "failedCount": 0,
  "successfulEmails": [
    "recruiter1@example.com",
    "recruiter2@example.com"
  ],
  "failedEmails": []
}
```

## Important

Email sending is disabled by default.

Before using `/send-all`, set:

```text
MAIL_SENDING_ENABLED=true
```

Test with your own email address first before adding a larger recruiter list.
