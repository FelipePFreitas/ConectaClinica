package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.config.RabbitMqConfig;
import com.felipefreitas.ConectaClinica.messaging.SenhaInicialEmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final RabbitTemplate rabbitTemplate;

    public void enviarSenhaInicial(String nome, String email, String login, String senha) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.NOTIFICATIONS_EXCHANGE,
                RabbitMqConfig.INITIAL_PASSWORD_ROUTING_KEY,
                new SenhaInicialEmailMessage(nome, email, login, senha)
        );
    }

    public void enviarSenhaInicialAgora(SenhaInicialEmailMessage emailMessage) {
        var message = new SimpleMailMessage();
        message.setTo(emailMessage.email());
        message.setSubject("Conecta Clínica - Acesso inicial");
        message.setText("""
                Olá, %s!

                Seu acesso à Conecta Clínica foi criado.

                Login: %s
                Senha temporária: %s

                Altere sua senha após o primeiro acesso.
                """.formatted(emailMessage.nome(), emailMessage.login(), emailMessage.senha()));

        mailSender.send(message);
    }
}