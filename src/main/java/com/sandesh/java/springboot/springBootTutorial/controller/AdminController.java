package com.sandesh.java.springboot.springBootTutorial.controller;

import com.sandesh.java.springboot.springBootTutorial.entity.User;
import com.sandesh.java.springboot.springBootTutorial.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserServices userServices;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userServices.findAllUsers();
        if (allUsers != null) {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdminUser(@RequestBody User admin) {
        User existingUser = userServices.findUserByUsername(admin.getUsername());
        if (existingUser != null) {
            return new ResponseEntity<>("This username is already Taken", HttpStatus.CONFLICT);
        }
        User adminUser = userServices.saveAdminUser(admin);
        return new ResponseEntity<>(adminUser, HttpStatus.OK);
    }

    // only admin user can create admin user. first admin user we save manually in database.
}
