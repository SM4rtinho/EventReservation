package com.example.application.data.repository;

import com.example.application.data.entity.EventReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventReservationRepository extends JpaRepository<EventReservation, Long> {
    List<EventReservation> findByUserId(Long userId);
    void deleteByUserIdAndEventId(Long userId, Long eventId);
}