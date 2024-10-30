package com.sandesh.java.springboot.springBootTutorial.controller;


import com.sandesh.java.springboot.springBootTutorial.entity.User;
import com.sandesh.java.springboot.springBootTutorial.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServices userServices;

//    ---------------------------------------------------------------------------------------------------------

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentUsername = authentication.getName();

        User userInDB = userServices.findUserByUsername(currentUsername);

        if (userInDB == null) {
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        }

        String newUsername = user.getUsername();
        if(newUsername.isEmpty()){
            return  new ResponseEntity<>("username can not be empty", HttpStatus.BAD_REQUEST);
        }
        if(userServices.findUserByUsername(newUsername) != null){
            return new ResponseEntity<>("username " + newUsername + "is already exists", HttpStatus.CONFLICT);
        }

        String newPassword = user.getPassword();
        if(newPassword.isEmpty()){
            return  new ResponseEntity<>("password can not be blank", HttpStatus.BAD_REQUEST);

        }
        userInDB.setUsername(newUsername);
        userInDB.setPassword(newPassword);

        userServices.saveNewUser(userInDB);

        return new ResponseEntity<>("username updated successfully", HttpStatus.OK);

    }

/*
    The SecurityContext is where Spring Security stores authentication details for the currently
     logged-in user(username we provide in Basic Auth).
     if it failed then un-authorized response is sent.

    SecurityContextHolder: This is a Spring Security class that holds the security context of the current user session
*/

    //    ---------------------------------------------------------------------------------------------------------

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userServices.deleteUserByUsername(username);
        return new ResponseEntity<>("user with username "+ username + " deleted successfully.", HttpStatus.OK);
    }

    //    ---------------------------------------------------------------------------------------------------------
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        User user = userServices.findUserByUsername(username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
    }


//    ----------------------------------------------------------------------------------------------------------

/*
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userServices.findAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
*/

/*
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
*/
}
