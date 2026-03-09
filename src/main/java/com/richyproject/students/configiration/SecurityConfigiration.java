package com.richyproject.students.configiration;


import com.richyproject.students.authentication.CustomAuthenticationFailureHandler;
import com.richyproject.students.authentication.CustomDaoAuthenticationProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfigiration {




   @Bean
    public PasswordEncoder passwordEncoder(){//creating a bean here so that can use the PasswordEncoder methods that are implemented by BEncryptPasswordEncoder class. we call these methods in the CustomUserDetailsService class

        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorization = new Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {
                authorizationManagerRequestMatcherRegistry
                        .requestMatchers(  "/DeleteEmployee","/AddNewEmployeePage","/DeleteStudentPage","/DeleteStudentAccomodationProfile","/UpdateStudent","/AddStudentPage","/SearchStudent","/DeleteStudentsAvailability")//come back to this other delete student endpoint as well"/DeleteStudentsAvailability")
                        .hasAnyRole("TEACHER")
                        .requestMatchers(  "/UpdateAccommodationProfile","/AccommodationProfile","/AgeRangePercentage","/AgeRange","/AverageGrades","/FindRoommate","StudentAvailabilityPage","/SearchStudent")
                        .hasAnyRole("TEACHER","STUDENT")
                        .anyRequest()//.authenticated();
                        .permitAll();
            }
        };
                Customizer<FormLoginConfigurer<HttpSecurity>> formLoginCustomizer = new Customizer<FormLoginConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer) {
                        httpSecurityFormLoginConfigurer
                                .loginPage("/LoginPage") 
                                //.failureUrl("/LoginPage?error=true") 
                                .failureHandler(new CustomAuthenticationFailureHandler())
                                .loginProcessingUrl("/login") 
                                .permitAll();

                    }

                };


        return http.authorizeHttpRequests(authorization)
                .formLogin(formLoginCustomizer)
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/ErrorPage?error=access");
                        }))
                .csrf(obj -> obj.disable())
                .build();

            }


            @Bean
            public AuthenticationProvider AuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {

                CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider();
                provider.setUserDetailsService(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder);

                return provider;
              

            }


        }





