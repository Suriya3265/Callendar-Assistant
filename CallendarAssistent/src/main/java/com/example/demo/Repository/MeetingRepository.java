package com.example.demo.Repository;

import com.example.demo.Models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
	
	
    List<Meeting> findByEmployeesId(Long id); // Use the correct attribute name
}
