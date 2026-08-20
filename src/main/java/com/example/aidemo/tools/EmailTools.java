package com.example.aidemo.tools;

import com.example.aidemo.services.EmailService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class EmailTools {
    private final EmailService emailService;

    public EmailTools(EmailService emailService) {
        this.emailService = emailService;
    }

    @Tool(description = "Send an email to a recipient")
    public String sendEmail(
          @ToolParam(description = "recipient id to which whom we are sending to ") String to,
          @ToolParam(description = "The subject for the email")  String subject,
          @ToolParam(description = "The body of a email with salutations at last")  String body
    ) {
        System.out.println("tried email tool");
        emailService.sendEmail(to, subject, body);

        return "Email sent successfully";
    }
}
