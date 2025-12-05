package com.yp.draw.service;

import com.yp.draw.entity.WinnerMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "${draw.queue.name}")
public class MQMessageListener {

    @Autowired
    private DrawBroadcastService drawBroadcastService;

    /**
     * 监听中奖消息并广播给本地WebSocket客户端
     */
    public void handleMessage(Object message) {
        System.out.println("[MQ Consumer] Received message: " + message);
        if (message instanceof WinnerMessage) {
            drawBroadcastService.broadcastWinner((WinnerMessage) message);
        } else if (message instanceof String) {
            drawBroadcastService.broadcastSystemNotice((String) message);
        } else {
            drawBroadcastService.broadcastText("未知消息类型: " + message.toString());
        }
    }
}