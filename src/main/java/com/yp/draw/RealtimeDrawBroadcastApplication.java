// File: RealtimeDrawBroadcastApplication.java
package com.yp.draw;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 核心注解，启动自动配置和组件扫描
@EnableRabbit // 启用RabbitMQ支持
public class RealtimeDrawBroadcastApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(RealtimeDrawBroadcastApplication.class, args);
        System.out.println("====== 实时抽奖广播服务已启动 ======");
        System.out.println("访问地址: http://localhost:8081/");
        System.out.println("健康检查: http://localhost:8081/health");
        System.out.println("WebSocket端点: /ws-draw");
    }

}