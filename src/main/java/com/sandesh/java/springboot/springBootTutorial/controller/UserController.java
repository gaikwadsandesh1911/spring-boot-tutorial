package com.sandesh.java.springboot.springBootTutorial.controller;


import com.sandesh.java.springboot.springBootTutorial.entity.User;
import com.sandesh.java.springboot.springBootTutorial.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserServices userServices;

    @PostMapping
    public ResponseEntity<?> createNewUser(@RequestBody User user) {
        User existingUser = userServices.findUserByUsername(user.getUsername()) ;
        if(existingUser != null){
            return new ResponseEntity<>("This username is already taken", HttpStatus.CONFLICT);
        }
        userServices.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        User user = userServices.findUserByUsername(username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userServices.findAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String username) {
        User userInDB = userServices.findUserByUsername(username);
//        System.out.println("userInDB" + userInDB);
        if (userInDB != null) {
            //                checking if providing user is not an empty and it is not same as current username
            if (!user.getUsername().isEmpty() && !user.getUsername().equals(username)) {
                //                checking if providing username is already in database
                User existingUser = userServices.findUserByUsername(user.getUsername());
//                If existingUser is not null, it means there is already another user in the system with the same username.
                if (existingUser != null) {
                    return new ResponseEntity<>(user.getUsername()+" is already exists", HttpStatus.CONFLICT);
                }
            }
            userInDB.setUsername(!user.getUsername().isEmpty() ? user.getUsername() : userInDB.getUsername());
            userInDB.setPassword(!user.getPassword().isEmpty() ? user.getPassword() : userInDB.getPassword());
            userServices.saveUser(userInDB);
            return new ResponseEntity<>("username updated to "+ userInDB.getUsername(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
    }

}
