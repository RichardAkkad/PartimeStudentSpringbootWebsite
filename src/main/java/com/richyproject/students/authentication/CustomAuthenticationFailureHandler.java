package com.richyproject.students.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Locked;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
        // if exception is thrown from the loadbyusername or possibly the retrieveuser method it is sent here via the exception parameter below
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

                if (exception instanceof LockedException && exception.getMessage().contains("expired")) {
                        response.sendRedirect("/LoginPage?error=expired");

                }
                else if(exception instanceof LockedException && exception.getMessage().contains("no expiry")) {
                        response.sendRedirect("/LoginPage?error=no expiry");

                }

                else if (exception instanceof LockedException && exception.getMessage().contains("hours")) {

                        response.sendRedirect("/LoginPage?error=hours");

                }

                else if(exception instanceof LockedException && exception.getMessage().contains("username")){
                        response.sendRedirect("/LoginPage?error=username");

                }
                else if(exception instanceof LockedException && exception.getMessage().contains("password")){
                        response.sendRedirect("/LoginPage?error=password");

                }


                return;



        }


}