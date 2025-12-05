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
    
    // Fanout交换机名称
    public static final String FANOUT_EXCHANGE = "broadcast.fanout.exchange";
    
    // 队列名称（模拟3个不同的消费者）
    public static final String QUEUE_A = "draw.queue.a";
    public static final String QUEUE_B = "draw.queue.b";
    public static final String QUEUE_C = "draw.queue.c";
    
    /**
     * 创建Fanout交换机
     * Fanout交换机会将消息广播到所有绑定的队列
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE, true, false); // 持久化，不自动删除
    }
    
    /**
     * 创建队列A
     */
    @Bean
    public Queue queueA() {
        return new Queue(QUEUE_A, true); // 持久化队列
    }
    
    /**
     * 创建队列B
     */
    @Bean
    public Queue queueB() {
        return new Queue(QUEUE_B, true);
    }
    
    /**
     * 创建队列C
     */
    @Bean
    public Queue queueC() {
        return new Queue(QUEUE_C, true);
    }
    
    /**
     * 绑定队列A到Fanout交换机
     */
    @Bean
    public Binding bindingA(Queue queueA, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queueA).to(fanoutExchange);
    }
    
    /**
     * 绑定队列B到Fanout交换机
     */
    @Bean
    public Binding bindingB(Queue queueB, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queueB).to(fanoutExchange);
    }
    
    /**
     * 绑定队列C到Fanout交换机
     */
    @Bean
    public Binding bindingC(Queue queueC, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queueC).to(fanoutExchange);
    }
    
    /**
     * JSON消息转换器
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        
        // 可选：设置回调用于调试
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("[Producer] 消息发送成功: " + correlationData);
            } else {
                System.out.println("[Producer] 消息发送失败: " + cause);
            }
        });
        
        return rabbitTemplate;
    }
}