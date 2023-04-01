package com.example.application.views;

import com.example.application.data.repository.UserRepository;
import com.example.application.data.service.AuthService;
import com.nimbusds.jose.proc.SecurityContext;
import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategy;

import com.example.application.data.entity.User;



import java.util.UUID;
@PageTitle("Chat")
@Route(value = "chats", layout = MainLayout.class)
@VaadinSessionScope
    public class Chat extends VerticalLayout {

        private UserRepository userRepository;

        public Chat(UserRepository userRepository) {
            this.userRepository = userRepository;
            addClassName("chat-view");
            setSpacing(false);

            User user = VaadinSession.getCurrent().getAttribute(User.class);
            String name = user.getUsername();
            UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), name);

            // Tabs allow us to change chat rooms.
            Tabs tabs = new Tabs(new Tab("#general"), new Tab("#support"), new Tab("#casual"));
            tabs.setWidthFull();


            CollaborationMessageList list = new CollaborationMessageList(userInfo, "chat/#general");
            list.setWidthFull();
            list.addClassNames("chat-view-message-list");


            CollaborationMessageInput input = new CollaborationMessageInput(list);
            input.addClassNames("chat-view-message-input");
            input.setWidthFull();

            // Layouting
            add(tabs, list, input);
            setSizeFull();
            expand(list);

            // Change the topic id of the chat when a new tab is selected
            tabs.addSelectedChangeListener(event -> {
                String channelName = event.getSelectedTab().getLabel();
                list.setTopic("chat/" + channelName);
            });
        }

    }

