package com.example.application.views;

import com.example.application.data.entity.User;
import com.example.application.data.service.AuthService;
import com.example.application.data.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

@VaadinSessionScope
@PageTitle("Password change")
@Route(value = "editprofile", layout = MainLayout.class)
public class EditProfileView extends VerticalLayout {


    private final UserService userService;
    private final AuthService authService;
    private User user;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField userNameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private PasswordField currentPasswordField;
    private Button saveButton;

    public EditProfileView(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
        user = VaadinSession.getCurrent().getAttribute(User.class);
        configureLayout();
        configureFields();
        configureButtons();
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    private void configureFields() {
        firstNameField = new TextField("Name:");
        firstNameField.setValue(user.getFirstName());
        add(firstNameField);

        lastNameField = new TextField("Surname:");
        lastNameField.setValue(user.getLastName());
        add(lastNameField);

        userNameField = new TextField("Username: ");
        userNameField.setValue(user.getUsername());
        add(userNameField);

        currentPasswordField = new PasswordField("Current password:");
        add(currentPasswordField);

        passwordField = new PasswordField("New password:");
        add(passwordField);

        confirmPasswordField = new PasswordField("Confirm password:");
        add(confirmPasswordField);
    }

    private void configureButtons() {
        saveButton = new Button("Save", event -> save());
        add(saveButton);
    }

    private void save() {
        if (!passwordField.getValue().equals(confirmPasswordField.getValue())) {
            Notification.show("Passwords do not match!");
        } else if(user.checkPassword(passwordField.getValue())){
            Notification.show("Current password is not correct!");
        }else {

                user.setFirstName(firstNameField.getValue());
                user.setLastName(lastNameField.getValue());
                String passwordSalt = RandomStringUtils.random(32);
                user.setPasswordSalt(passwordSalt);
                user.setPasswordHash(DigestUtils.sha1Hex(passwordField.getValue() + passwordSalt));
                userService.saveUser(user);
                Notification.show("Changes saved!");
        }
    }
}