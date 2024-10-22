package com.sandesh.java.springboot.springBootTutorial.controller;


import com.sandesh.java.springboot.springBootTutorial.entity.JournalV2;
import com.sandesh.java.springboot.springBootTutorial.entity.User;
import com.sandesh.java.springboot.springBootTutorial.services.JournalServices;
import com.sandesh.java.springboot.springBootTutorial.services.UserServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<JournalV2>> getAllJournal() {
        List<JournalV2> allEntries = journalServices.getAllEntries();
        if (allEntries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allEntries, HttpStatus.OK);
    }

    @PostMapping("/user/{username}")
    public ResponseEntity<?> createEntry(@RequestBody JournalV2 newEntry, @PathVariable String username) {
        try {
            newEntry.setDate(LocalDateTime.now());
            journalServices.saveEntry(newEntry, username);
            return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
        } catch (Exception e) {
//            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getAllJournalsOfUser(@PathVariable String username) {
        User user = userServices.findUserByUsername(username);
        List<JournalV2> allEntriesOfUser = user.getJournalEntries();
        if (allEntriesOfUser != null && !allEntriesOfUser.isEmpty()) {
            return new ResponseEntity<>(allEntriesOfUser, HttpStatus.OK);
        }
        return new ResponseEntity<>("No journals found for the user.", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalV2> getJournalById(@PathVariable ObjectId id) {
        Optional<JournalV2> journalEntry = journalServices.getEntryById(id);
        return journalEntry.map(journalV2 -> new ResponseEntity<>(journalV2, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PutMapping("/{username}/{journalId}")
    public ResponseEntity<JournalV2> updateJournalById(@PathVariable ObjectId journalId, @PathVariable String username, @RequestBody JournalV2 entryToUpdate) {
        Optional<JournalV2> oldEntryOpt = journalServices.getEntryById(journalId);

        if (oldEntryOpt.isPresent()) {
            JournalV2 oldEntry = oldEntryOpt.get();
            oldEntry.setTitle(!entryToUpdate.getTitle().isEmpty() ? entryToUpdate.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(!entryToUpdate.getContent().isEmpty() ? entryToUpdate.getContent() : oldEntry.getContent());
            oldEntry.setDate(LocalDateTime.now());
            journalServices.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{username}/{journalId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId journalId, @PathVariable String username) {
        Optional<JournalV2> entry = journalServices.getEntryById(journalId);
        if (entry.isPresent()) {
            journalServices.deleteEntryById(journalId, username);
            return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Entry not found", HttpStatus.NOT_FOUND);
        }

    }

    // when we delete journal, still it's reference exists in users collection when we use mongodb, so we have to delete it manually.
    // but with relational database it's get deleted automatically.

}

/*
    controller => service => repository
*/