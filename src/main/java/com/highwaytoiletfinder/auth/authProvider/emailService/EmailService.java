package com.highwaytoiletfinder.auth.authProvider.emailService;

import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.exceptions.MailerSendException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${mailersend.api.token}")
    private String apiToken;

    @Value("${mailersend.from.email}")
    private String fromEmail;

    @Value("${mailersend.from.name}")
    private String fromName;

    /** Sends an email using a MailerSend template.
     *
     * @param toName Recipient's name
     * @param toEmail Recipient's email
     * @param templateId MailerSend template ID
     * @param token Password reset token (or other customization data)
     */
    public void sendPasswordResetEmail(String toName, String toEmail, String templateId, String token) {

        Email email = new Email();

        // Remetente
        email.setFrom(fromName, fromEmail);

        // Recipient
        email.addRecipient(toName, toEmail);

        // Defines the template
        email.setTemplateId(templateId);

        // Adds personalization
        email.setSubject("Redefinição de senha - Highway Toilet Finder");
        email.addPersonalization("name", toName);
        email.addPersonalization("token", token);

        // creates the client
        MailerSend ms = new MailerSend();
        ms.setToken(apiToken);

        try {
            MailerSendResponse response = ms.emails().send(email);
            System.out.println("Email successfully sent! MessageId: " + response.messageId);
        } catch (MailerSendException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email via MailerSend", e);
        }
    }
}
