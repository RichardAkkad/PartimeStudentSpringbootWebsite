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
                        .loginPage("/LoginPage") // ← when user tries to go to a restricted endpoint then redirect to this page.
                        //.failureUrl("/LoginPage?error") // ← If login fails, redirect here with error so param picks it up in the login page
                        .failureHandler(new CustomAuthenticationFailureHandler())//the bean here is used to call the onAuthenticationFailureHandler method after the locked exception if thrown back up the call stack
                        .loginProcessingUrl("/login") // ← Form submits and then we come to this line here (Spring handles automatically this endpoint lines 66-72 is a idea of the code for this), can write anything here not just "/login"
                        .permitAll();// ← Allow access to all login-related URLs

            }//When you don't specify loginProcessingUrl(), Spring Security defaults to using the same URL as your login page. so When you don't specify "loginProcessingUrl()"
            // then GET /login → Shows the login form and POST /login → Processes the login (Spring Security handles automatically)

        };
        return http.authorizeHttpRequests(authorization).formLogin(formLoginCustomizer).exceptionHandling(ex ->
                ex.accessDeniedHandler((request, response, accessDeniedException) -> {response.sendRedirect("/ErrorPage?error=access");}))
                .csrf(obj -> obj.disable()).oauth2Login(oauth2 -> oauth2
                .loginPage("/LoginPage")
                .userInfoEndpoint(u -> u.userService(oauth2UserService()))
        ).build();

    }

        @Bean
        public AuthenticationProvider AuthenticationProvider (UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){//the object for this second parameter is coming from the @bean method Password at the top

            CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider();
            provider.setUserDetailsService(userDetailsService);// this is a field unlike passwordEncoder below, both are still objects however
            provider.setPasswordEncoder(passwordEncoder);// so this here just creates a bEncrypt object so that I can call the matches method in the customDao...class and in the additionalAuthenticationChecks method in the Dao.... class
            //Its so that we can use the matches method earlier in my custom authenticate method instead of waiting for it to be used in the matches method in the addidtionauthenticationchecks method only in the Dao..... class, matches method will be used twice here if no locked exception is thrown
            //also note that the locked exception get thrown back up the call stack to authenticate method in the providermanager class and then to the attemptauthentication method in the usernamepasswordauthenticationfilter class
            // and then to a doFilter class which apparently catches it where a failureHandler method  somewhere does something with it.

            return provider;// if a bean if just a object then i am assuming that its just then object "CustomDao......." with the reference type as Authentication Provider without a variable name, i think "provider is used as the variable name (from the authenticate method in the provider manager class)


//question why are we using "!this.passwordEncoder" in the additionalAuthenticationChecks method to call the matches method? surely we can just use autowired to retrieve
            // bean and use that to call this matches method in the additional........... method?
        }


    }



