package com.example.application.data.service;

import com.example.application.data.entity.Event;

import com.example.application.data.entity.User;
import com.example.application.data.repository.EventRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;



    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;

    }



    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }


    public Optional<Event> getEventById(long id) {
        return eventRepository.findById(id);
    }
}