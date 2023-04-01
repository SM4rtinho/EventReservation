package com.example.application.views.admin;

import com.example.application.data.entity.Event;
import com.example.application.data.service.EventService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import javax.annotation.security.PermitAll;
@VaadinSessionScope
@PageTitle("Event edit")
@PermitAll
@Route(value = "eventedit", layout = MainLayout.class)
public class EventView extends VerticalLayout {

    private final EventService eventService;
    private Grid<Event> eventGrid;
    private Button addEventButton;
    EventFormView eventForm;

    public EventView(EventService eventService) {
        this.eventService =eventService;
        eventGrid = new Grid<>(Event.class);
        setSizeFull();
        configureEventGrid();
        configureForm();
        Button addContactButton = new Button("Add event");




        add(getContent(), addContactButton);

        addContactButton.addClickListener(e -> addEvent());
        updateList();
        closeEditor();

        eventGrid.addItemClickListener(event -> showEvent(event.getItem()));
    }
    private void configureEventGrid() {
        eventGrid.setSizeFull();
        eventGrid.setColumns("name", "organizer", "location");

        eventGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        eventGrid.asSingleSelect().addValueChangeListener(e -> editEvent(e.getValue()));

    }
    private void editEvent(Event event) {
        if(event == null){
            closeEditor();
        }else{
            eventForm.setEvent(event);
            eventForm.setVisible(true);
            addClassName("editing");
        }
    }
    private void closeEditor() {
        eventForm.setEvent(null);
        eventForm.setVisible(false);
        removeClassName("editing");
    }
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(eventGrid, eventForm);
        content.setFlexGrow(2, eventGrid);
        content.setFlexGrow(1, eventForm);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    private void configureForm() {
        eventForm = new EventFormView();
        eventForm.setWidth("25em");

        eventForm.addListener(EventFormView.SaveEvent.class, this::saveContact);
        eventForm.addListener(EventFormView.DeleteEvent.class, this::deleteContact);
    }



    private void showEvent(Event event) {
        eventService.updateEvent(event);
        Long eventId = event.getId();

    }
    private void deleteContact(EventFormView.DeleteEvent event){
        eventService.deleteEvent(event.getEvent());
        updateList();
        closeEditor();
    }
    public  void saveContact(EventFormView.SaveEvent event) {
        eventService.saveEvent(event.getEvent());
        updateList();
    }
    private void updateList() {
        eventGrid.setItems(eventService.getAllEvents());
    }

    private void addEvent() {
        eventGrid.asSingleSelect().clear();
        editEvent(new Event());
    }

}