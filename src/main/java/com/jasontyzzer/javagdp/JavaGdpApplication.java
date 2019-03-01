package com.jasontyzzer.javagdp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JavaGdpApplication {
    public static final String EXCHANGE_NAME = "CountryGDP";
    public static final String QUEUE_NAME = "Log";

    public static void main(String[] args) {
        SpringApplication.run(JavaGdpApplication.class, args);
    }

    @Bean
    public TopicExchange appExchange(){
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue appQueue(){
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding bindQueue(){
        return BindingBuilder.bind(appQueue()).to(appExchange()).with(QUEUE_NAME);
    }

    @Bean
    public RabbitTemplate rt(final ConnectionFactory connectionFactory){
        final RabbitTemplate rt = new RabbitTemplate(connectionFactory);
        rt.setMessageConverter(jackson2JsonMessageConverter());
        return rt;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}