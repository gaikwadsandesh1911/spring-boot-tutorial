package com.sandesh.java.springboot.springBootTutorial.controller;

import com.sandesh.java.springboot.springBootTutorial.entity.User;
import com.sandesh.java.springboot.springBootTutorial.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class PublicController {

    @Autowired
    UserServices userServices;

    @GetMapping("public/health-check")
    public String healthCheck(){
        return  "Hello World!";
    }

    @PostMapping("public/create-user")
    public ResponseEntity<?> createNewUser(@RequestBody User user) {
        User existingUser = userServices.findUserByUsername(user.getUsername());
        if(existingUser != null){
            return new ResponseEntity<>("This username is already taken", HttpStatus.CONFLICT);
        }
        userServices.saveNewUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
