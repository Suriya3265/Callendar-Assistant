package com.example.demo.Model;

import java.time.LocalDateTime;
import java.util.List;

public class MeetingRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<Long> participantIds; 
    
    public MeetingRequest() {}

    public MeetingRequest(LocalDateTime startTime, LocalDateTime endTime, List<Long> participantIds) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.participantIds = participantIds;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<Long> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
    }
}
