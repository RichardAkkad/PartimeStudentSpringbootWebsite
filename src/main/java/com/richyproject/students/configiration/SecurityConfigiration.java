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

                        // with ".anyRequest().permitAll()" everything else is open to everyone and also the login page doesnt appear because no reason to redirect to a login page because everything is already accessible.But with
                        // anyRequest().authenticated()" everything else requires including "/index" endpoint (the homepage) and so the  login page appears. with "anyRequest.permitAll()"The login page exists, but you're never sent
                        // there because you don't need to authenticate. login page appears because you have ".authenticated()", I think this method triggers the login page at the start.
                        .permitAll();//we go to this line first ,"permitAll" here means check the endpoints above, if eg we go to eg "/DeletePage" then we need authorisation,
                //this triggers the "loginPage("LoginPage")" (which relates to a login page) and then goes to the LoginPage html login page. Then when we press submit on the login page (where we have entered some login details )
                //we then go to line 45(because the login page has "action="login"),because we need to check this endpoint if we have access to it, and it has permitAll so we can access this endpoint and
                // and then we go to line 72 ".loginProcessingUrl("/login")" and then this makes spring takes care of this method/Postmapping endpoint(line 57).(Also it doesnt have to be login, can write anything here eg "log")
                //Spring Security automatically creates POST /login endpoint, POST /login - processes username/password. Authentication logic,Success/failure redirect and Session creation
                //@PostMapping("/login")
                //public void handleLogin(String username, String password) {
                //    // Authentication logic
                //    // Session creation
                //    // Redirect logic
                //}
                //if we did "loginprocessingUrl("/log")" then the method would have the mapping "/log" instead
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




