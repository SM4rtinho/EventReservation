package com.example.application.views;

import com.example.application.data.entity.Event;
import com.example.application.data.entity.User;
import com.example.application.data.service.EventReservationService;
import com.example.application.data.service.EventService;
import com.example.application.data.service.UserService;
import com.example.application.views.admin.EventFormView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import java.util.List;

@VaadinSessionScope
@PageTitle("Events")
@PermitAll
@Route(value = "events", layout = MainLayout.class)
public class EventsView extends VerticalLayout {

    private final EventService eventService;
    private final UserService userService;
    private final EventReservationService eventReservationSetvice;
    private Grid<Event> eventGrid;
    private User user;
    private Label title;
    private Button showReservations;

    public EventsView(EventService eventService, UserService userService, EventReservationService eventReservationService) {
        this.eventService = eventService;
        this.userService = userService;
        this.eventReservationSetvice = eventReservationService;
        user = VaadinSession.getCurrent().getAttribute(User.class);
        configureLayout();
        configureGrid();
        configureButtons();
        updateEvents();
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        title = new Label("Your events:");
        add(title);
    }

    private void configureGrid() {
        eventGrid = new Grid<>();
        eventGrid.setSizeFull();
        eventGrid.addColumn(Event::getName).setHeader("Event name");
        eventGrid.addColumn(Event::getLocation).setHeader("Event location");

        eventGrid.addComponentColumn(this::createCancelReservationButton).setHeader("Cancel reservation");
        eventGrid.setHeight("300px");
        add(eventGrid);
    }

    private void configureButtons() {
        showReservations = new Button("Show your reservations", e -> updateEvents());
        add(showReservations);
    }

    private void updateEvents() {
        List<Event> events = eventReservationSetvice.getUserReservations(user.getId());
        eventGrid.setItems(events);
    }

    private Button createCancelReservationButton(Event event) {
        Button cancelReservationButton = new Button("Cancel reservation");
        cancelReservationButton.addClickListener(e -> {
            eventReservationSetvice.cancelReservation(user.getId(), event.getId());
            updateEvents();
        });
        return cancelReservationButton;
    }
}