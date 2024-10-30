package com.sandesh.java.springboot.springBootTutorial.services;

import com.sandesh.java.springboot.springBootTutorial.entity.User;
import com.sandesh.java.springboot.springBootTutorial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username); // our custom method to find user by username
        if(user != null){
            // here we construct userDetails instance
            return org.springframework.security.core.userdetails.User.builder()  // here we construct userDetails instance
                    .username(user.getUsername())
                    .password(user.getPassword())   // the hashed password from db, will encode with PasswordEncoder passwordEncoder()
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
        }
        throw  new UsernameNotFoundException("user not found with username: "+ username);
    }

}

/*
   UserDetailsService interface is a core component responsible for retrieving user-related data.

    1>  UserDetailsService interface has single method loadUserByUsername()`this method is call by sprig security to load user details
        when username is provided for authentication in Auth => Basic Auth username

    2>  User.builder(): This is a static builder method from Spring Security's User class.
        is used to construct a UserDetails instance

    3>  username(user.getUsername()): Sets the username in the UserDetails object
        using the username from the User object retrieved from the database.

    4>  By building and returning a UserDetails instance,
        we provide Spring Security with essential information it needs to validate and authorize the user,
*/
