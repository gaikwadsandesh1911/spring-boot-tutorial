package com.sandesh.java.springboot.springBootTutorial.controller;

import com.sandesh.java.springboot.springBootTutorial.entity.JournalV2;
import com.sandesh.java.springboot.springBootTutorial.entity.User;
import com.sandesh.java.springboot.springBootTutorial.services.JournalServices;
import com.sandesh.java.springboot.springBootTutorial.services.UserServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/journal")
public class JournalControllerV2 {
    @Autowired
    private JournalServices journalServices;
    @Autowired
    private UserServices userServices;

//    ===================================================================================================

    @GetMapping("/all")
    public ResponseEntity<List<JournalV2>> getAllJournal() {
        List<JournalV2> allEntries = journalServices.getAllEntries();
        if (allEntries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allEntries, HttpStatus.OK);
    }
//    ===================================================================================================

    /*@PostMapping("/journal/{username}")
    public ResponseEntity<?> createEntry(@RequestBody JournalV2 newEntry, @PathVariable String username) {
        try {
//            newEntry.setDate(LocalDateTime.now());
            journalServices.saveEntry(newEntry, username);
            return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("exception "+ e.getMessage());
            return new ResponseEntity<>("Error while creating entry", HttpStatus.BAD_REQUEST);
        }
    }*/

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalV2 newEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            journalServices.saveEntry(newEntry, username);
            return new ResponseEntity<>(newEntry, HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println("exception " + e.getMessage());
            return new ResponseEntity<>("Error while creating entry", HttpStatus.BAD_REQUEST);
        }
    }
//    ===================================================================================================

    /*@GetMapping("/journal/{username}")
    public ResponseEntity<?> getAllJournalsOfUser(@PathVariable String username) {
        User user = userServices.findUserByUsername(username);
        List<JournalV2> allEntriesOfUser = user.getJournalEntries();
        if (allEntriesOfUser != null && !allEntriesOfUser.isEmpty()) {
            return new ResponseEntity<>(allEntriesOfUser, HttpStatus.OK);
        }
        return new ResponseEntity<>("No journals found for the user.", HttpStatus.NOT_FOUND);
    }*/


    @GetMapping
    public ResponseEntity<?> getAllJournalsOfUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userServices.findUserByUsername(username);

        List<JournalV2> allEntriesOfUser = user.getJournalEntries();

        if (allEntriesOfUser != null && !allEntriesOfUser.isEmpty()) {
            return new ResponseEntity<>(allEntriesOfUser, HttpStatus.OK);
        }
        return new ResponseEntity<>("No journals found for the user.", HttpStatus.NOT_FOUND);
    }

    //    ===================================================================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getJournalById(@PathVariable ObjectId id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userServices.findUserByUsername(username);

        List<JournalV2> entry = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).toList();
//        System.out.println("entry "+ entry);

        if (!entry.isEmpty()) {
            Optional<JournalV2> journalEntry = journalServices.getEntryById(id);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Entry not found", HttpStatus.NOT_FOUND);

    }

    //    ===================================================================================================
    @PutMapping("/{journalId}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId journalId, @RequestBody JournalV2 entryToUpdate) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userServices.findUserByUsername(username);

        List<JournalV2> existingEntry = user.getJournalEntries().stream().filter(x -> x.getId().equals(journalId)).toList();

        if (!existingEntry.isEmpty()) {

            Optional<JournalV2> journalEntry = journalServices.getEntryById(journalId);

            if (journalEntry.isPresent()) {

                JournalV2 oldEntry = journalEntry.get();

                // Update fields only if they are not empty
                if (!entryToUpdate.getTitle().isEmpty()) {
                    oldEntry.setTitle(entryToUpdate.getTitle());
                }
                if (!entryToUpdate.getContent().isEmpty()) {
                    oldEntry.setContent(entryToUpdate.getContent());
                }
                oldEntry.setDate(LocalDateTime.now());
                journalServices.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Entry not found in journal service", HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>("Entry not Found in user's journalEntries", HttpStatus.NOT_FOUND);
    }


    //    ===================================================================================================
    @DeleteMapping("/{journalId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId journalId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean deleted = journalServices.deleteEntryById(journalId, username); // deleteEntryById() returns boolean val.
        if (deleted) {
            return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("No entry found!", HttpStatus.NOT_FOUND);
    }
// when we delete journal, still it's reference exists in users collection when we use mongodb, so we have to delete it manually.
// but with relational database it's get deleted automatically.

}

/*
    controller => service => repository
*/
