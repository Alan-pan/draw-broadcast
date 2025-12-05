package com.yp.draw.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class WinnerMessage implements Serializable {
    private String userId;
    private String userName;
    private String prizeName;
    private Timestamp timestamp;
    private String activityId;  // 活动ID
    private String level;       // 奖项等级

    public String toAnnouncementText() {
        StringBuilder sb = new StringBuilder();
        sb.append("🎉 ");
        
        if (level != null && !level.isEmpty()) {
            sb.append("【").append(level).append("】");
        }
        
        sb.append("恭喜用户 ").append(this.userName).append(" 抽中 ").append(this.prizeName).append("!");
        return sb.toString();
    }
    
    public String toSimpleText() {
        return "恭喜 " + this.userName + " 获得 " + this.prizeName;
    }
}