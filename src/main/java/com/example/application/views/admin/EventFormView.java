package com.example.application.views.admin;

import com.example.application.data.entity.Event;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class EventFormView extends FormLayout {
    Binder<Event> binder = new BeanValidationBinder<>(Event.class);

    TextField name = new TextField("Name");
    TextArea description = new TextArea("Description");
    TextField location = new TextField("Location");
    TextField organizer = new TextField("Organizer");
    DatePicker startDatePicker = new DatePicker("Start date");

    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Button delete = new Button("Delete");
    private Event event;

    public EventFormView(){
        binder.bindInstanceFields(this);

        add(name,
                description,
                location,
                organizer,
                startDatePicker,

                createButtonsLayout());

    }
    public void setEvent(Event event){
        this.event = event;
        binder.readBean(event);
    }
    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new EventFormView.DeleteEvent(this, this.event)));
        close.addClickListener(event -> fireEvent(new EventFormView.CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);



        return new HorizontalLayout(save, delete, close);
    }
    private void validateAndSave() {
        try{
            binder.writeBean(event);
            fireEvent(new SaveEvent(this, event));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    public static abstract class EventFormEvent extends ComponentEvent<EventFormView> {
        private Event event;

        protected EventFormEvent(EventFormView source, Event event) {
            super(source, false);
            this.event = event;
        }

        public Event getEvent() {
            return event;
        }
    }
    public static class SaveEvent extends EventFormView.EventFormEvent {
        SaveEvent(EventFormView source, Event event) {
            super(source, event);
        }
    }
    public static class DeleteEvent extends EventFormView.EventFormEvent {
        DeleteEvent(EventFormView source, Event event) {
            super(source, event);
        }

    }


    public static class CloseEvent extends EventFormView.EventFormEvent {
        CloseEvent(EventFormView source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }



}
