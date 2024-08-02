package com.jobbi.me.security;

import com.jobbi.me.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    private static final String LOGIN_URL = "/login";
    private static final String HOME_URL = "/home";
    private static final String LOGOUT_SUCCESS_URL = LOGIN_URL + "?logout";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/static/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher(LOGIN_URL)).permitAll()
        );

        super.configure(http);

        http.oauth2Login(oauth2 -> oauth2
                .loginPage(LOGIN_URL)
                .defaultSuccessUrl(HOME_URL, true)
                .permitAll()
        );

        setLoginView(http, LoginView.class);
    }
}
