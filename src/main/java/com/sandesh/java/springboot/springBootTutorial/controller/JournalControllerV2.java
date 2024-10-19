package com.sandesh.java.springboot.springBootTutorial.controller;


import com.sandesh.java.springboot.springBootTutorial.entity.JournalV2;
import com.sandesh.java.springboot.springBootTutorial.services.JournalServices;
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

    @PostMapping
    public ResponseEntity<JournalV2> createEntry(@RequestBody JournalV2 newEntry) {
        newEntry.setDate(LocalDateTime.now());
        journalServices.saveEntry(newEntry);
        return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JournalV2>> getAll() {
        List<JournalV2> allEntries = journalServices.getAllEntries();
        if (allEntries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allEntries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalV2> getById(@PathVariable ObjectId id) {
        Optional<JournalV2> entry = journalServices.getEntryById(id);

        return entry.map(journal -> new ResponseEntity<>(journal, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalV2> updateById(@PathVariable ObjectId id, @RequestBody JournalV2 entryToUpdate) {
        Optional<JournalV2> oldEntryOpt = journalServices.getEntryById(id);
        if (oldEntryOpt.isPresent()) {
            JournalV2 oldEntry = oldEntryOpt.get();
            oldEntry.setTitle(entryToUpdate.getTitle() != null && !entryToUpdate.getTitle().isEmpty() ? entryToUpdate.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(entryToUpdate.getContent() != null && !entryToUpdate.getContent().isEmpty() ? entryToUpdate.getContent() : oldEntry.getContent());
            oldEntry.setDate(LocalDateTime.now());

            journalServices.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable ObjectId id) {
        Optional<JournalV2> entry = journalServices.getEntryById(id);
        if (entry.isPresent()) {
            journalServices.deleteEntryById(id);
            return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Entry not found", HttpStatus.NOT_FOUND);
        }

    }

}

/*
    controller => service => repository
*/