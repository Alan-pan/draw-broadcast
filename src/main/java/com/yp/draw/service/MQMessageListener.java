package com.yp.draw.service;

import com.yp.draw.config.RabbitMQConfig;
import com.yp.draw.entity.WinnerMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQMessageListener {

    @Autowired
    private DrawBroadcastService drawBroadcastService;

    /**
     * 监听中奖消息并广播给本地WebSocket客户端
     */
    @RabbitListener(queues = "#{drawQueue.name}")
    public void handleWinnerMessage(WinnerMessage winnerMessage) {
        System.out.println("[MQ Consumer] Received winner message: " + winnerMessage);
        drawBroadcastService.broadcastWinner(winnerMessage);
    }

    /**
     * 监听系统通知并广播给本地WebSocket客户端
     */
    @RabbitListener(queues = "#{drawQueue.name}")
    public void handleSystemNotice(String notice) {
        System.out.println("[MQ Consumer] Received system notice: " + notice);
        drawBroadcastService.broadcastSystemNotice(notice);
    }
}