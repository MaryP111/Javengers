package gr.ece.ntua.javengers.controller;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class MailController {
    @Autowired
    private JavaMailSender sender;

    @PostMapping(value="/home/sendMail")
    public  RedirectView sendMail(String messageBody, Model model) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo("maryparelli@gmail.com");
            helper.setText(messageBody);
            helper.setSubject("Mail From  User");
        } catch (MessagingException e) {
            e.printStackTrace();
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/home");
            return redirectView;

        }
        sender.send(message);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/home");
        return  redirectView;

    }
}
