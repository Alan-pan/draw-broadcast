package com.yp.draw.service;

import com.yp.draw.entity.WinnerMessage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LotteryService {

    private List<String> participants = new ArrayList<>();

    public synchronized void addParticipant(String participant) {
        if (participant != null && !participant.trim().isEmpty()) {
            participants.add(participant.trim());
        }
    }

    public synchronized void removeParticipant(String participant) {
        participants.remove(participant);
    }

    public synchronized List<String> drawWinners(int count) {
        if (participants.isEmpty() || count <= 0) {
            return Collections.emptyList();
        }

        List<String> winners = new ArrayList<>();
        List<String> candidatePool = new ArrayList<>(participants);
        
        // 确保抽取数量不超过参与者数量
        int actualCount = Math.min(count, candidatePool.size());

        Random random = new Random();
        for (int i = 0; i < actualCount; i++) {
            int index = random.nextInt(candidatePool.size());
            winners.add(candidatePool.remove(index));
        }

        return winners;
    }

    public synchronized List<String> getAllParticipants() {
        return new ArrayList<>(participants);
    }

    public synchronized void clearParticipants() {
        participants.clear();
    }
    
    public synchronized int getParticipantCount() {
        return participants.size();
    }
    
    /**
     * 按权重抽奖（预留功能）
     * @param weights 参与者对应的权重
     * @param count 抽奖数量
     * @return 中奖者列表
     */
    public synchronized List<String> drawWinnersWithWeight(Map<String, Integer> weights, int count) {
        // 简单实现，实际项目中可以根据权重进行抽奖
        return drawWinners(count);
    }
}