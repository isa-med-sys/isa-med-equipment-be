package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.beans.User;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(User user, String confirmationLink) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("medicalequipment753@gmail.com");
        mailMessage.setSubject("Complete your registration!");
        mailMessage.setText("To be able to log into your account, please click on the following link: " + confirmationLink);
        javaMailSender.send(mailMessage);
    }
}