package com.example.demo.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;


import com.example.demo.Model.MeetingRequest;
import com.example.demo.Model.TimeSlot;
import com.example.demo.Models.Employee;
import com.example.demo.Models.Meeting;
import com.example.demo.Repository.EmployeeRepository;
import com.example.demo.Repository.MeetingRepository;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private MeetingRepository repository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    private final LocalTime WorkStart=LocalTime.of(9, 0);
    private final LocalTime WorkEnd=LocalTime.of(18, 0);
    
    public boolean bookMeeting(Meeting meeting) {

    	if(isWorkingHours(meeting)) {
    		return false;
    	}
        List<Meeting> existingMeetings = repository.findAll();
        for (Meeting existingMeeting : existingMeetings) {
            if (existingMeeting.getStartTime().isBefore(meeting.getEndTime()) &&
                meeting.getStartTime().isBefore(existingMeeting.getEndTime())) {
                return false;
            }
            
        }
        repository.save(meeting);
        return true;
    }
    
    public boolean isWorkingHours(Meeting meeting) {
    	
      
    	LocalTime startTime=meeting.getStartTime().toLocalTime();
    	LocalTime endTime=meeting.getEndTime().toLocalTime();
    	
    	return !startTime.isBefore(WorkStart) && !endTime.isAfter(WorkEnd);
    }

    public List<TimeSlot> findFreeSlots(String emp1, String emp2, int duration) {
        List<Meeting> emp1Meetings = fetchEmployeeMeetings(emp1);
        List<Meeting> emp2Meetings = fetchEmployeeMeetings(emp2);

        List<TimeSlot> emp1FreeSlots = findFreeSlots(emp1Meetings);
        List<TimeSlot> emp2FreeSlots = findFreeSlots(emp2Meetings);

        List<TimeSlot> commonFreeSlots = new ArrayList<>();
        for (TimeSlot slot1 : emp1FreeSlots) {
            for (TimeSlot slot2 : emp2FreeSlots) {
                LocalDateTime latestStart = Collections.max(List.of(slot1.getStartTime(), slot2.getStartTime()));
                LocalDateTime earliestEnd = Collections.min(List.of(slot1.getEndTime(), slot2.getEndTime()));
                if (latestStart.isBefore(earliestEnd) && Duration.between(latestStart, earliestEnd).toMinutes() >= duration) {
                    commonFreeSlots.add(new TimeSlot(latestStart, latestStart.plusMinutes(duration)));
                }
            }
        }
        return commonFreeSlots;
    }

    public List<Meeting> findConflicts(MeetingRequest request) {
        List<Meeting> existingMeetings = repository.findAll();
       
        return existingMeetings.stream()
                .filter(meeting -> meeting.getStartTime().isBefore(request.getEndTime()) &&
                        request.getStartTime().isBefore(meeting.getEndTime())&&isWorkingHours(meeting))
                .collect(Collectors.toList());
    }

    private List<TimeSlot> findFreeSlots(List<Meeting> meetings) {
        LocalDateTime workdayStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0));
        LocalDateTime workdayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0));

        List<TimeSlot> freeSlots = new ArrayList<>();
        Collections.sort(meetings, Comparator.comparing(Meeting::getStartTime));

        if (!meetings.isEmpty() && workdayStart.isBefore(meetings.get(0).getStartTime())) {
            freeSlots.add(new TimeSlot(workdayStart, meetings.get(0).getStartTime()));
        }

        for (int i = 0; i < meetings.size() - 1; i++) {
            LocalDateTime endOfCurrentMeeting = meetings.get(i).getEndTime();
            LocalDateTime startOfNextMeeting = meetings.get(i + 1).getStartTime();
            if (endOfCurrentMeeting.isBefore(startOfNextMeeting)) {
                freeSlots.add(new TimeSlot(endOfCurrentMeeting, startOfNextMeeting));
            }
        }

        if (!meetings.isEmpty() && meetings.get(meetings.size() - 1).getEndTime().isBefore(workdayEnd)) {
            freeSlots.add(new TimeSlot(meetings.get(meetings.size() - 1).getEndTime(), workdayEnd));
        }

        return freeSlots;
    }

    private List<Meeting> fetchEmployeeMeetings(String employeeId) {
    	  Long id = Long.parseLong(employeeId);
          return repository.findByEmployeesId(id);
    }
    
    
}

