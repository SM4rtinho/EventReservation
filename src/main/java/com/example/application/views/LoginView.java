package com.example.application.views;

import com.example.application.data.entity.User;
import com.example.application.data.service.AuthService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

@Route(value = "")
@RouteAlias(value = "login")
@PageTitle("Login")
@VaadinSessionScope
public class LoginView extends VerticalLayout {


    public LoginView(AuthService authService) {

        setId("login-view");

        var username = new TextField("Username");
        var password = new PasswordField("Password");


        add(
                new H1("Science Conferences"),
                username,
                password,
                new Button("Login", event -> {
                    try{
                        authService.authenticate(username.getValue(), password.getValue());
                        UI.getCurrent().navigate("homer");
                    } catch(AuthService.AuthException e){
                        Notification.show("Wrong credentials");
                    }
                }),
                new RouterLink("Register", RegisterView.class)


        );
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);



    }
}

