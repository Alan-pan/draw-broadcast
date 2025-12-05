package com.yp.draw.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DRAW_FANOUT_EXCHANGE = "draw.fanout";
    public static final String DRAW_QUEUE_PREFIX = "draw.queue.";

    /**
     * 创建Fanout交换机
     */
    @Bean
    public FanoutExchange drawFanoutExchange() {
        return new FanoutExchange(DRAW_FANOUT_EXCHANGE);
    }

    /**
     * 创建队列（每个实例独立的队列）
     */
    @Bean
    public Queue drawQueue() {
        // 每个应用实例创建唯一的队列
        return new Queue(DRAW_QUEUE_PREFIX + java.util.UUID.randomUUID().toString(), false);
    }

    /**
     * 绑定队列到交换机
     */
    @Bean
    public Binding binding(Queue drawQueue, FanoutExchange drawFanoutExchange) {
        return BindingBuilder.bind(drawQueue).to(drawFanoutExchange);
    }

    /**
     * 配置RabbitTemplate使用JSON转换器
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * JSON消息转换器
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}