package com.dac.client.client_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void enviarSenhaPorEmail(String emailDestino, String senha) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDestino);
        message.setSubject("Sua senha de acesso ao sistema da Empresa Aérea");
        message.setText("Olá! Seu cadastro foi realizado com sucesso. Sua senha de acesso é: " + senha);

        emailSender.send(message);
    }
}
