package com.felipefreitas.ConectaClinica.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void enviarSenhaInicial(String nome, String email, String login, String senha) {
        var message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Conecta Clínica - Acesso inicial");
        message.setText("""
                Olá, %s!

                Seu acesso à Conecta Clínica foi criado.

                Login: %s
                Senha temporária: %s

                Altere sua senha após o primeiro acesso.
                """.formatted(nome, login, senha));

        mailSender.send(message);
    }
}