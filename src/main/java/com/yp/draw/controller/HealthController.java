// File: HealthController.java
package com.yp.draw.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基础健康检查和系统状态控制器
 */
@RestController
public class HealthController {

    /**
     * 用于检查服务是否成功启动
     * 访问地址: http://localhost:8081/health
     */
    @GetMapping("/health")
    public String checkHealth() {
        return "Real-time Draw Broadcast Service is running! WebSocket endpoint: /ws-draw";
    }
    
    // 如果需要一个 HTTP 接口来手动触发一次广播，可以添加类似如下方法
    /*
    @Autowired
    private DrawBroadcastService broadcastService;

    @GetMapping("/manual-trigger")
    public String trigger() {
        // 模拟一条手动消息
        WinnerMessage msg = new WinnerMessage();
        msg.setUserName("管理员");
        msg.setPrizeName("测试推送");
        msg.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
        broadcastService.broadcastWinner(msg);
        return "Manual message triggered and broadcasted.";
    }
    */
}