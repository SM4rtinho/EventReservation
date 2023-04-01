package com.example.application.data.service;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.example.application.data.repository.UserRepository;
import com.example.application.views.*;
import com.example.application.views.admin.EventView;
import com.example.application.views.admin.ListView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    public record AuthorizedRoute(String route, String name, Class<? extends Component> view){

    }

    public class AuthException extends Exception{

    }

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public void authenticate(String username, String password) throws AuthException{
        User user = userRepository.getByUsername(username);
        VaadinSession.getCurrent().setAttribute(User.class,user);
        if(user != null && user.checkPassword(password)){
            createRoutes(user.getRole());
        }else {
            throw new AuthException();
        }
    }

    private void createRoutes(Role role) {
        getAuthorizedRoutes(role).stream()
                .forEach(route ->
                        RouteConfiguration.forSessionScope().setRoute(
                                route.route, route.view, MainLayout.class));
    }
    public List<AuthorizedRoute> getAuthorizedRoutes(Role role){
        var routes = new ArrayList<AuthorizedRoute>();
        if(role.equals(Role.USER)){
            routes.add(new AuthorizedRoute("homer","Home", HomeView.class));
            routes.add(new AuthorizedRoute("chats","Chat", Chat.class));
            routes.add(new AuthorizedRoute("reservation","Reservation", ReservationView.class));
            routes.add(new AuthorizedRoute("events", "Events", EventsView.class));
            routes.add(new AuthorizedRoute("editprofile", "Password change", EditProfileView.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));

        }
        if (role.equals(Role.ADMIN)){
            routes.add(new AuthorizedRoute("homer","Home", HomeView.class));
            routes.add(new AuthorizedRoute("admin","Admin", ListView.class));
            routes.add(new AuthorizedRoute("eventedit","Event edit", EventView.class));
            routes.add(new AuthorizedRoute("chats","Chat", Chat.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));

        }
        return routes;
    }
    public void register(String firstName, String lastName, String username, String password ){
        userRepository.save(new User(firstName, lastName,  username, password, Role.USER));
    }

    public int count(){
        return 1;
    }
}
