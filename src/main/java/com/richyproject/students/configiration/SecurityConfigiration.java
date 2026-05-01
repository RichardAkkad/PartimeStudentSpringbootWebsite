package com.richyproject.students.configiration;


import com.richyproject.students.authentication.CustomAuthenticationFailureHandler;
import com.richyproject.students.authentication.CustomDaoAuthenticationProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;


import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@EnableWebSecurity
public class  SecurityConfigiration {

    @Bean
    public PasswordEncoder Password() {//creating a bean here so that can use the PasswordEncoder methods that are implemented by BEncryptPasswordEncoder class. we call these methods in the CustomUserDetailsService class

        return new BCryptPasswordEncoder();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return request -> {
            OAuth2User user = delegate.loadUser(request);
            String registrationId = request.getClientRegistration().getRegistrationId();
            String nameAttribute = registrationId.equals("github") ? "id" : "email";
            return new DefaultOAuth2User(
                    List.of(new SimpleGrantedAuthority("ROLE_STUDENT")),
                    user.getAttributes(),
                    nameAttribute
            );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorization = new Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {
                authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/DeleteEmployee", "/AddNewEmployeePage", "/DeleteStudentPage", "/DeleteStudentAccomodationProfile", "/DeleteStudentsAvailability", "/SearchStudent", "/UpdateStudent")//come back to this other delete student endpoint as well"/DeleteStudentsAvailability")
                        .hasAnyRole("TEACHER")
                        .requestMatchers("/UpdateAccommodationProfile", "/AccommodationProfile", "/AgeRangePercentage", "/AgeRange", "/AverageGrades", "/FindRoommate", "StudentAvailabilityPage", "/AddStudentPage")
                        .hasAnyRole("TEACHER", "STUDENT")
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

            }//When you don't specify loginProcessingUrl(), Spring Security defaults to using the same URL as your login page. so When you don't specify "loginProcessingUrl()"
            // then GET /login → Shows the login form and POST /login → Processes the login (Spring Security handles automatically)

        };
        return http.authorizeHttpRequests(authorization).formLogin(formLoginCustomizer).exceptionHandling(ex ->
                ex.accessDeniedHandler((request, response, accessDeniedException) -> {response.sendRedirect("/ErrorPage?error=access");}))
                .csrf(obj -> obj.disable()).oauth2Login(oauth2 -> oauth2
                .loginPage("/LoginPage")
                .userInfoEndpoint(u -> u.userService(oauth2UserService()))
        ).build();

                CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider();
                provider.setUserDetailsService(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder);

                return provider;
              

            return provider;// if a bean if just a object then i am assuming that its just then object "CustomDao......." with the reference type as Authentication Provider without a variable name, i think "provider is used as the variable name (from the authenticate method in the provider manager class)


//question why are we using "!this.passwordEncoder" in the additionalAuthenticationChecks method to call the matches method? surely we can just use autowired to retrieve
            // bean and use that to call this matches method in the additional........... method?
        }


    }





