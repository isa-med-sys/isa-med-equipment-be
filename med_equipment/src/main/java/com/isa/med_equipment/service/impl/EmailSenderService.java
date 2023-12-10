package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.model.User;
import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    private static final String FROM_EMAIL = "medicalequipment753@gmail.com";
    private static final String CONTENT_TYPE = "application/octet-stream";

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(User user, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom(FROM_EMAIL);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }

    @Async
    public void sendEmailWithAttachment(User user, String subject, String message, byte[] attachment, String attachmentName) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(user.getEmail());
        helper.setFrom(FROM_EMAIL);
        helper.setSubject(subject);
        helper.setText(message);

        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setText(message);

        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        attachmentBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachment, CONTENT_TYPE)));
        attachmentBodyPart.setFileName(attachmentName);

        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(textBodyPart);
        multipart.addBodyPart(attachmentBodyPart);

        mimeMessage.setContent(multipart);

        javaMailSender.send(mimeMessage);
    }
}