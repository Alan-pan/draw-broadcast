package com.yp.draw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // 启用 WebSocket 消息代理功能
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 配置消息代理 (Message Broker)
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 客户端订阅地址的前缀，所有客户端订阅的地址都必须以 /topic 开头
        config.enableSimpleBroker("/topic"); 
        
        // 客户端发送消息到服务器的地址前缀，这里不需要客户端发送，但保留规范
        config.setApplicationDestinationPrefixes("/app"); 
    }

    /**
     * 注册 STOMP 端点
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册 /ws-draw 作为 STOMP 端点，客户端将通过它连接
        // .withSockJS() 用于兼容不支持原生 WebSocket 的浏览器
        registry.addEndpoint("/ws-draw").withSockJS();
    }
}