package com.example.application.data.service;

import com.example.application.data.entity.Event;
import com.example.application.data.entity.EventReservation;
import com.example.application.data.entity.User;
import com.example.application.data.repository.EventReservationRepository;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EventReservationService {
    @Autowired
    private EventReservationRepository eventReservationRepository;

    public List<Event> getUserReservations(Long userId) {
        List<EventReservation> eventReservations = eventReservationRepository.findByUserId(userId);
        List<Event> events = new ArrayList<>();
        eventReservations.forEach(eventReservation -> events.add(eventReservation.getEvent()));
        return events;
    }
    public void reserveEvent(Event event, User user) {
        List<EventReservation> userReservations = eventReservationRepository.findByUserId(user.getId());
        for (EventReservation reservation : userReservations) {
            if(reservation.getEvent().equals(event)){
                throw new IllegalArgumentException("You have already booked this event");
            }
        }
        EventReservation newReservation = new EventReservation(user, event);
        eventReservationRepository.save(newReservation);
        Notification.show("Event successfully booked!");
    }
    public void cancelReservation(Long userId, Long eventId) {
        eventReservationRepository.deleteByUserIdAndEventId(userId, eventId);
        Notification.show("Reservation successfully canceled!");
    }

}