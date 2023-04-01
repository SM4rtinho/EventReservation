package com.example.application.views;

import com.example.application.data.service.AuthService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import javax.annotation.security.PermitAll;

@PermitAll
@PageTitle("Register")
@Route("register")
@VaadinSessionScope
public class RegisterView extends Composite {
    private final AuthService authService;

    public RegisterView(AuthService authService){
        this.authService = authService;
    }

    @Override
    protected Component initContent(){
        TextField firstName = new TextField("First name");
        TextField lastName = new TextField("Last name");
        TextField username = new TextField("Username");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm Password");

        return new VerticalLayout(
                new H2("Register"),
                firstName,
                lastName,
                username,
                password1,
                password2,
                new Button("Send", event -> register(
                        firstName.getValue(),
                        lastName.getValue(),
                        username.getValue(),
                        password1.getValue(),
                        password2.getValue()
                )),
                new Button("Login", event -> UI.getCurrent().getPage().setLocation("login"))

        );

    }

    private void register(String firstName, String lastName,
                          String username, String password1, String password2) {
        if(firstName.trim().isEmpty()){
            Notification.show("Enter a first name");
        }
        else if(lastName.trim().isEmpty()){
            Notification.show("Enter a last name");
        }
        else if(username.trim().isEmpty()){
            Notification.show("Enter a username");
        }
        else if(password1.trim().isEmpty()){
            Notification.show("Enter a password");
        }
        else if (!password1.equals(password2)) {
            Notification.show("Passwords do not match");
        }
        authService.register(firstName, lastName, username, password1);
        Notification.show("You registered sucessfully!");


    }
}
