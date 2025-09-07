package com.highwaytoiletfinder.auth.authProvider;

import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.Recipient;
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

    /**
     * Envia um email usando um template do MailerSend.
     *
     * @param toName      Nome do destinatário
     * @param toEmail     Email do destinatário
     * @param templateId  ID do template do MailerSend
     * @param token       Token de redefinição de senha (ou outro dado de personalização)
     */
    public void sendPasswordResetEmail(String toName, String toEmail, String templateId, String token) {

        Email email = new Email();

        // Remetente
        email.setFrom(fromName, fromEmail);

        // Destinatário
        email.addRecipient(toName, toEmail);

        // Define o template
        email.setTemplateId(templateId);

        // Adiciona personalização
        email.setSubject("Redefinição de senha - Highway Toilet Finder");
        email.addPersonalization("name", toName);
        email.addPersonalization("token", token);

        // Cria o client
        MailerSend ms = new MailerSend();
        ms.setToken(apiToken);

        try {
            MailerSendResponse response = ms.emails().send(email);
            System.out.println("Email enviado com sucesso! MessageId: " + response.messageId);
        } catch (MailerSendException e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao enviar email via MailerSend", e);
        }
    }
}
