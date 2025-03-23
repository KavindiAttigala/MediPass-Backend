package com.sdgp.MediPass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendMediIdEmail(String email, long mediId, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("MediPass Registration is Successful");
        message.setText("Welcome to MediPass\n" +
                "Dear"+firstName+", \n\n"+"You have successfully completed the registration process of MediPass application. Now to proceed further login with your MediID: "+mediId+"\n\n"+"We recommend that you keep this MediID confidential.\n"+"Thank you for using MediPass Application");
    }
}

