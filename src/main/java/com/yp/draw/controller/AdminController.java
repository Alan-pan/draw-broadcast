package com.yp.draw.controller;

import com.yp.draw.service.DrawBroadcastService;
import com.yp.draw.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private DrawBroadcastService broadcastService;

    @PostMapping("/broadcast")
    public String broadcastMessage(@RequestParam String message) {
        broadcastService.broadcastText(message);
        return "Message broadcasted successfully";
    }

    @PostMapping("/notice")
    public String sendNotice(@RequestParam String notice) {
        broadcastService.broadcastSystemNotice(notice);
        return "Notice sent successfully";
    }

    @PostMapping("/clear-participants")
    public String clearParticipants() {
        lotteryService.clearParticipants();
        return "Participants cleared successfully";
    }

    @GetMapping("/stats")
    public String getStats() {
        int participantCount = lotteryService.getAllParticipants().size();
        return String.format("当前系统统计:\n- 参与者数量: %d", participantCount);
    }
}