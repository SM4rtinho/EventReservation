package com.example.application.views;


import com.example.application.components.appnav.AppNav;
import com.example.application.components.appnav.AppNavItem;
import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.example.application.data.service.AuthService;
import com.example.application.views.admin.EventView;
import com.example.application.views.admin.ListView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */

public class MainLayout extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;
    private AuthService authService;

    public MainLayout(AuthService authService) {
        this.authService = authService;
        setPrimarySection(Section.DRAWER);


        createHeader();
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private void createHeader() {
        H1 space = new H1("");
        space.addClassNames("text-l", "m-m");

        viewTitle = new H1();

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), viewTitle, space );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(space);

        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(true, header);

    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        H1 text = new H1("Menu");
        logoLayout.add(text);
        layout.add(logoLayout, menu);
        layout.setAlignSelf(FlexComponent.Alignment.CENTER, logoLayout);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {

        var user = VaadinSession.getCurrent().getAttribute(User.class);
        return authService.getAuthorizedRoutes(user.getRole()).stream()
                .map(r -> createTab(r.name(), r.view()))
                .toArray(Component[]::new);
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren()
                .filter(tab -> ComponentUtil.getData(tab, Class.class)
                        .equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}