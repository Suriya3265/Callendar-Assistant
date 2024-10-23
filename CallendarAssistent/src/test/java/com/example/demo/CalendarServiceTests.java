package com.example.demo;

import com.example.demo.Model.MeetingRequest;
import com.example.demo.Model.TimeSlot;
import com.example.demo.Models.Meeting;
import com.example.demo.Service.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CalendarServiceTests {

    @Autowired
    private Service calendarService;

    @Test
    public void testBookMeeting() {
        Meeting meeting = new Meeting();
        meeting.setTitle("Test Meeting");
        meeting.setStartTime(LocalDateTime.of(2024, 10, 21, 10, 0));
        meeting.setEndTime(LocalDateTime.of(2024, 10, 21, 11, 0));
        meeting.setEmployees(Arrays.asList()); // Add appropriate employee data

        boolean isBooked = calendarService.bookMeeting(meeting);
        assertTrue(isBooked, "The meeting should be booked successfully.");
    }

    @Test
    public void testFindFreeSlots() {
        String emp1 = "1";
        String emp2 = "2";
        int duration = 30; // Duration in minutes

        List<TimeSlot> freeSlots = calendarService.findFreeSlots(emp1, emp2, duration);
        assertNotNull(freeSlots, "Free slots should not be null.");
        assertTrue(freeSlots.size() > 0, "There should be at least one free slot available.");
    }

    @Test
    public void testFindConflicts() {
        MeetingRequest request = new MeetingRequest();
        request.setStartTime(LocalDateTime.of(2024, 10, 21, 10, 0));
        request.setEndTime(LocalDateTime.of(2024, 10, 21, 11, 0));
        request.setParticipantIds(Arrays.asList(1L, 2L)); // Add appropriate participant IDs

        List<Meeting> conflicts = calendarService.findConflicts(request);
        assertNotNull(conflicts, "Conflicts should not be null.");
        assertTrue(conflicts.size() > 0, "There should be at least one conflict.");
    }
}
