package com.richyproject.students.Configiration;


import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


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
                authorizationManagerRequestMatcherRegistry//.requestMatchers("/LoginPage").permitAll() ......<- this means allow anyone to the login page without authorisation
                        .requestMatchers("/login").permitAll()
                        .requestMatchers( "/DeletePage", "/AddNewEmployeePage","/DeleteEmployee","/SearchStudentPage")
                        .hasAnyRole("TEACHER","STUDENT")
                        .requestMatchers("/AgeRange", "/AgeRangePercentage", "/AverageGrades","/AddStudentPage","/UpdateStudentPage","/AccommodationProfile","/FindRoommate","/UpdateAccommodationProfile")
                        .hasAnyRole("TEACHER","STUDENT")
                        .anyRequest()
                        .permitAll();
            }
        };


        Customizer<FormLoginConfigurer<HttpSecurity>> formLoginCustomizer=new Customizer<FormLoginConfigurer<HttpSecurity>>() {
            @Override
            public void customize(FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer) {
                httpSecurityFormLoginConfigurer.
                        loginPage("/LoginPage"). // ← When login needed, redirect to this page.
                        failureUrl("/LoginPage?error=true"). // ← If login fails, redirect here with error
                        loginProcessingUrl("/login") // ← Form submits here (Spring handles automatically)
                        .permitAll();// ← Allow access to all login-related URLs
                        //anything to do with "loginpage" when the login page is activated involves the stuff in this method

            }

        };


        return http.authorizeHttpRequests(authorization).formLogin(formLoginCustomizer).csrf(obj->obj.disable()).build();


    }


            @Bean
            public AuthenticationProvider Authenticationprovider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {

                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setUserDetailsService(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder);
                return provider;

            }





}



