package com.sandesh.java.springboot.springBootTutorial.config;

import com.sandesh.java.springboot.springBootTutorial.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/public/**").permitAll() // to be accessed by anyone, without any authentication.
                        .requestMatchers("/journal/**", "/user/**").authenticated() // user must log in but no specific role required
                        .requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated() // Only users with the ADMIN role can access URLs that start with
                )
                .userDetailsService(customUserDetailsService)
                .httpBasic(Customizer.withDefaults())
                .build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }*/
    // we can directly chain .userDetailsService(customUserDetailsService) on SecurityFilterChain



}

/*
    once we added a 'spring-boot-starter-security' dependency all endpoints will be secure by default.
    with username as user and password is randomly generated at console.
    we have to sent username and password in postman => Authorization => Basic Auth
                or
    by customize username and password in application.properties file

    #spring.security.user.name=sandesh
    #spring.security.user.password=Sandesh@19

    SecurityFilterChain basically setting up different security rules for different endpoints
    or specify how requests are authenticated and authorized.

    It also specifies which UserDetailsService should be used to fetch user details (credentials, roles, etc.) via
     userDetailsService(customUserDetailsService).

    we customize our endpoint with .requestMatchers() what to secure and what to not.


*/

/*
    PasswordEncoder is an interface and BCryptPasswordEncoder is its implementation class. Bcrypt is hashing algorithm

    passwordEncoder() method return instance of BCryptPasswordEncoder class.
    and responsible for hashing and encoding user password

    .userDetailsService(customUserDetailsService)
     customUserDetailsService is implementation class for UserDetailsService interface.
     UserDetailsService interface is a core component responsible for retrieving user-related data
*/

/*
    By setting the session creation policy to STATELESS,
    Spring Security will not create or use an HTTP session to store any security-related information,
    making it suitable for stateless applications (like those using JWT tokens for authentication).
*/