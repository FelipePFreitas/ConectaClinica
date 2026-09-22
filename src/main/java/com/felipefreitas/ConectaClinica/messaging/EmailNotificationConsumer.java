package com.felipefreitas.ConectaClinica.messaging;

import com.felipefreitas.ConectaClinica.config.RabbitMqConfig;
import com.felipefreitas.ConectaClinica.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMqConfig.INITIAL_PASSWORD_QUEUE)
    public void consumirSenhaInicial(SenhaInicialEmailMessage message) {
        emailService.enviarSenhaInicialAgora(message);
    }
}
