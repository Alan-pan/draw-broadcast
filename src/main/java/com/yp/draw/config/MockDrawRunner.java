package com.yp.draw.config;

import com.yp.draw.entity.WinnerMessage;
import com.yp.draw.service.DrawBroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MockDrawRunner implements ApplicationRunner {

    @Autowired
    private DrawBroadcastService broadcastService;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final List<String> userNames = Arrays.asList("张三", "李四", "王五", "赵六", "钱七");
    private final List<String> prizes = Arrays.asList("iPhone 16 Pro", "MacBook Pro", "AirPods Pro", "小米手环", "谢谢参与");
    private int counter = 0;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Mock 抽奖服务启动，每 10 秒模拟产生一条中奖记录...");
        
        // 每 10 秒执行一次模拟抽奖和推送，降低频率避免干扰正常操作
        scheduler.scheduleAtFixedRate(this::mockAndBroadcast, 10, 10, TimeUnit.SECONDS);
    }

    private void mockAndBroadcast() {
        // 模拟随机选择中奖者和奖品
        String winner = userNames.get(counter % userNames.size());
        String prize = prizes.get((int) (Math.random() * prizes.size()));

        WinnerMessage msg = new WinnerMessage();
        msg.setUserId("U" + counter);
        msg.setUserName(winner + String.format("%03d", counter)); // 确保名称唯一性
        msg.setPrizeName(prize);
        msg.setTimestamp(new Timestamp(System.currentTimeMillis()));
        
        // 触发广播
        broadcastService.broadcastWinner(msg);
        counter++;
    }
    
    // 销毁时关闭调度器
    public void destroy() {
        scheduler.shutdown();
    }
}