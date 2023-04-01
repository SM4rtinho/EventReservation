package com.example.application.data.service;

import com.example.application.data.entity.User;
import com.example.application.data.repository.UserRepository;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vaadin.crudui.crud.CrudListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements CrudListener<User> {

    private final UserRepository userRepository;


    public List<User> findAllUsers(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return userRepository.findAll();
        } else {
            return userRepository.search(stringFilter);
        }
    }
    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User add(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void delete(User user) {

    }


    public long countUsers() {
        return userRepository.count();
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void saveUser(User user) {
        if (user == null) {
            System.err.println("User is null. Are you sure you have connected your form to the application?");
            return;
        }
        userRepository.save(user);
    }
    public void editData(User user, String firstname, String lastname, String username) {
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setUsername(username);
        userRepository.save(user);
    }
    public Collection<User> findAllUsername () {
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        String username = user.getUsername();
        Collection<User> users = findAll();
        Collection<User> users2 = new ArrayList<>();
        for (User u : users) {
            if(u.getUsername().equals(username)) {
                users2.add(u);
            }
        }
        return users2;
    }


}
