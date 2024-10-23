package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.MeetingRequest;
import com.example.demo.Model.TimeSlot;
import com.example.demo.Models.Meeting;
import com.example.demo.Service.Service;

@RestController
public class Controller {
	
	@Autowired
	private Service service;
	
	@PostMapping("/book")
	public ResponseEntity<String> bookMeeting(@RequestBody Meeting meeting){
		boolean isBooked=service.bookMeeting(meeting);
		return isBooked ? ResponseEntity.ok("Meeting Booking") : ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict detected");
	}
	
	@GetMapping("/free-slots")
	public ResponseEntity<List<TimeSlot>> findFreeSlots(@RequestParam String emp1, @RequestParam String emp2, @RequestParam int duration) {
	    List<TimeSlot> freeSlots = service.findFreeSlots(emp1, emp2, duration);
	    return ResponseEntity.ok(freeSlots);
	}
	
	@GetMapping("/conflicts")
	public ResponseEntity<List<Meeting>> findConflicts(@RequestBody MeetingRequest request) {
	    List<Meeting> conflicts =service.findConflicts(request);
	    return ResponseEntity.ok(conflicts);
	}


}
