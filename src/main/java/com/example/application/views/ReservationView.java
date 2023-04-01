package com.example.application.views;

import com.example.application.data.entity.Event;
import com.example.application.data.entity.User;
import com.example.application.data.service.EventReservationService;
import com.example.application.data.service.EventService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import javax.annotation.security.PermitAll;

@VaadinSessionScope
@PageTitle("Reservation")
@PermitAll
@Route(value = "reservation", layout = MainLayout.class)
public class ReservationView extends VerticalLayout {
    private final EventService eventService;
    private ComboBox<Event> eventComboBox;
    private Button reserveButton;
    private Button showDescriptionButton;
    private Label descriptionLabel;
    private Label startDateLabel;
    private final EventReservationService eventReservationService;

    public ReservationView(EventService eventService, EventReservationService eventReservationService){
        this.eventService = eventService;
        this.eventReservationService = eventReservationService;
        eventComboBox = new ComboBox<>("Select event:");
        reserveButton = new Button("Reserve");
        showDescriptionButton = new Button("Show description");
        descriptionLabel = new Label();
        startDateLabel = new Label();


        setSizeFull();
        configureEventComboBox();
        configureButtons();
        updateEventList();
        add(eventComboBox, reserveButton, showDescriptionButton, descriptionLabel,startDateLabel);
    }
    private void configureEventComboBox() {
        eventComboBox.setItemLabelGenerator(Event::getName);
        eventComboBox.setWidth("300px");
    }

    private void configureButtons() {
        reserveButton.addClickListener(event -> {
            Event selectedEvent = eventComboBox.getValue();
            User user = VaadinSession.getCurrent().getAttribute(User.class);
            if(selectedEvent != null) {
                try {
                    eventReservationService.reserveEvent(selectedEvent, user);
                    Notification.show("Event successfully booked!");
                } catch (IllegalArgumentException e) {
                    Notification.show(e.getMessage());
                }
            } else {
                Notification.show("Please select an event to book");
            }
        });
        showDescriptionButton.addClickListener(e -> showDescription());
    }
    private void updateEventList() {
        eventComboBox.setItems(eventService.getAllEvents());
    }



    private void showDescription() {
        Event selectedEvent = eventComboBox.getValue();
        if (selectedEvent != null) {
            descriptionLabel.setText(selectedEvent.getDescription());
            startDateLabel.setText("Date: " + selectedEvent.getStartDate().toString());
            add(descriptionLabel, startDateLabel);
            add(descriptionLabel);
        } else {
            Notification.show("Please select an event to show description.");
        }
    }
}

