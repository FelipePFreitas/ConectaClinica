package com.felipefreitas.ConectaClinica.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String NOTIFICATIONS_EXCHANGE = "conectaclinica.notifications";
    public static final String INITIAL_PASSWORD_QUEUE = "conectaclinica.email.initial-password";
    public static final String INITIAL_PASSWORD_ROUTING_KEY = "email.initial-password";

    @Bean
    TopicExchange notificationsExchange() {
        return new TopicExchange(NOTIFICATIONS_EXCHANGE, true, false);
    }

    @Bean
    Queue initialPasswordEmailQueue() {
        return QueueBuilder.durable(INITIAL_PASSWORD_QUEUE).build();
    }

    @Bean
    Binding initialPasswordEmailBinding(Queue initialPasswordEmailQueue, TopicExchange notificationsExchange) {
        return BindingBuilder.bind(initialPasswordEmailQueue)
                .to(notificationsExchange)
                .with(INITIAL_PASSWORD_ROUTING_KEY);
    }

    @Bean
    Jackson2JsonMessageConverter rabbitMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
