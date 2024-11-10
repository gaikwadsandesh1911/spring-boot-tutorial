package com.sandesh.java.springboot.springBootTutorial.services;

import com.sandesh.java.springboot.springBootTutorial.entity.User;
import com.sandesh.java.springboot.springBootTutorial.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Slf4j
@Service
public class UserServices {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

//    private static final Logger logger = (Logger) LoggerFactory.getLogger(UserServices.class);    // @Slf4j

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveNewUser(User user) {
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
        }catch(Exception e){
//            logger.info(e.getMessage());
            log.error("Error occurred for : {} {}", user.getUsername(), e.getMessage());
        }
    }

    public User saveAdminUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepository.save(user);
        return user;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteUserByUsername(String username){
        userRepository.deleteByUsername(username);
    }

}
