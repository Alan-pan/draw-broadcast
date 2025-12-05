package com.yp.draw.service;

import com.yp.draw.config.RabbitMQConfig;
import com.yp.draw.entity.WinnerMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送中奖消息到MQ
     */
    public void sendWinnerMessage(WinnerMessage winnerMessage) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", winnerMessage);
        System.out.println("[MQ Producer] Sent winner message: " + winnerMessage);
    }

    /**
     * 发送系统通知到MQ
     */
    public void sendSystemNotice(String notice) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", notice);
        System.out.println("[MQ Producer] Sent system notice: " + notice);
    }
}