package com.jobbi.me.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@RouteAlias("")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView() {

        setClassName("login-view");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        Image logo = new Image("images/login.png", "logo");
        logo.setWidth("150px");

        H1 title = new H1("Welcome To My Technical Test");
        Paragraph description = new Paragraph("Please log in to continue.");

        Button loginButton = new Button("Login with Google", event -> {
            UI.getCurrent().getPage().setLocation("/oauth2/authorization/google");
        });

        add(logo, title, description, loginButton);
    }

}
