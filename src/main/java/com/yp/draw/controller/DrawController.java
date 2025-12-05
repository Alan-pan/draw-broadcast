package com.yp.draw.controller;

import com.yp.draw.entity.WinnerMessage;
import com.yp.draw.service.LotteryService;
import com.yp.draw.service.MQProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/draw")
public class DrawController {

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private MQProducerService mqProducerService;

    @PostMapping("/add-participant")
    public String addParticipant(@RequestParam String name) {
        lotteryService.addParticipant(name);
        return "Participant added successfully";
    }

    @PostMapping("/remove-participant")
    public String removeParticipant(@RequestParam String name) {
        lotteryService.removeParticipant(name);
        return "Participant removed successfully";
    }

    @GetMapping("/participants")
    public List<String> getParticipants() {
        return lotteryService.getAllParticipants();
    }

    @PostMapping("/start")
    public String startDraw(@RequestParam(required = false, defaultValue = "1") int winnerCount,
                           @RequestParam(required = false, defaultValue = "大奖") String prizeName) {
        List<String> winners = lotteryService.drawWinners(winnerCount);

        if (winners.isEmpty()) {
            return "No participants available for drawing";
        }

        for (String winner : winners) {
            WinnerMessage message = new WinnerMessage();
            message.setUserId(UUID.randomUUID().toString());
            message.setUserName(winner);
            message.setPrizeName(prizeName);
            message.setTimestamp(new Timestamp(System.currentTimeMillis()));

            // 发送到MQ而不是直接广播
            mqProducerService.sendWinnerMessage(message);
        }

        return "Draw completed. Winners: " + String.join(", ", winners);
    }

    @PostMapping("/announce")
    public String announceWinner(@RequestParam String userName,
                                @RequestParam String prizeName) {
        WinnerMessage message = new WinnerMessage();
        message.setUserId(UUID.randomUUID().toString());
        message.setUserName(userName);
        message.setPrizeName(prizeName);
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));

        // 发送到MQ而不是直接广播
        mqProducerService.sendWinnerMessage(message);
        return "Winner announced successfully";
    }
    
    @PostMapping("/clear")
    public String clearParticipants() {
        lotteryService.clearParticipants();
        // 发送系统通知到MQ
        mqProducerService.sendSystemNotice("参与者列表已清空");
        return "Participants cleared successfully";
    }
}