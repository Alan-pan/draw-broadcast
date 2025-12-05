// File: DrawBroadcastService.java
package com.yp.draw.service;

import com.yp.draw.entity.WinnerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class DrawBroadcastService {

    // SimpMessagingTemplate 是 Spring 提供的工具，用于向 STOMP 客户端发送消息
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 定义广播给所有客户端的订阅通道
    private static final String ANNOUNCEMENT_TOPIC = "/topic/announcements";

    /**
     * 接收中奖消息，并广播给所有订阅者
     * (在实际生产中，这个方法会在 Kafka/MQ 监听器中被调用)
     */
    public void broadcastWinner(WinnerMessage winnerMessage) {
        String announcementText = winnerMessage.toAnnouncementText();
        
        // 使用 convertAndSend 将消息发送到指定的目的地
        // 所有订阅了 /topic/announcements 的客户端都会收到这条消息
        messagingTemplate.convertAndSend(ANNOUNCEMENT_TOPIC, announcementText);
        
        System.out.println("[WebSocket Broadcast] Sent: " + announcementText);
    }
    
    /**
     * 发送简单文本消息
     */
    public void broadcastText(String text) {
        messagingTemplate.convertAndSend(ANNOUNCEMENT_TOPIC, text);
        System.out.println("[WebSocket Broadcast] Sent: " + text);
    }
    
    /**
     * 发送系统通知
     */
    public void broadcastSystemNotice(String notice) {
        String systemMessage = "📢 系统通知: " + notice;
        messagingTemplate.convertAndSend(ANNOUNCEMENT_TOPIC, systemMessage);
        System.out.println("[WebSocket Broadcast] Sent: " + systemMessage);
    }
}