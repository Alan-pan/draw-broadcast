package com.yp.draw.service;

import com.yp.draw.config.RabbitMQConfig;
import com.yp.draw.entity.WinnerMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class MQMessageListener {

    @Autowired
    private DrawBroadcastService drawBroadcastService;

    /**
     * 监听中奖消息并广播给本地WebSocket客户端
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_A)
    public void handleMessageA(@Payload WinnerMessage message) {
        if (message instanceof WinnerMessage) {
            System.out.println("[MQ ConsumerA] Received WinnerMessage: " + message);
            drawBroadcastService.broadcastWinner((WinnerMessage) message);
        }else{
            System.out.println("[MQ ConsumerA] Unknown message type: " + message.getClass());
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_B)
    public void handleMessageB(Object message) {
        System.out.println("[MQ ConsumerB] Received message: " + message);
//        if (message instanceof WinnerMessage) {
//            drawBroadcastService.broadcastWinner((WinnerMessage) message);
//        } else if (message instanceof String) {
//            drawBroadcastService.broadcastSystemNotice((String) message);
//        } else {
//            drawBroadcastService.broadcastText("未知消息类型: " + message.toString());
//        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_C)
    public void handleMessageC(Object message) {
        System.out.println("[MQ ConsumerC] Received message: " + message);
//        if (message instanceof WinnerMessage) {
//            drawBroadcastService.broadcastWinner((WinnerMessage) message);
//        } else if (message instanceof String) {
//            drawBroadcastService.broadcastSystemNotice((String) message);
//        } else {
//            drawBroadcastService.broadcastText("未知消息类型: " + message.toString());
//        }
    }
}