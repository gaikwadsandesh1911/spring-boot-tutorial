package com.sandesh.java.springboot.springBootTutorial.controller;


import com.sandesh.java.springboot.springBootTutorial.entity.JournalV2;
import com.sandesh.java.springboot.springBootTutorial.services.JournalServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
    public JournalV2 createEntry(@RequestBody JournalV2 newEntry) {
        newEntry.setDate(LocalDateTime.now());
        journalServices.saveEntry(newEntry);
        return newEntry;
    }

    @GetMapping
    public List<JournalV2> getAll() {
        return journalServices.getAllEntries();
    }

    @GetMapping("/{id}")
    public JournalV2 getById(@PathVariable ObjectId id) {
        return journalServices.getEntryById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable ObjectId id) {
        journalServices.deleteEntryById(id);
        return "deleted successfully";
    }

    @PutMapping("/{id}")
    public JournalV2 updateById(@PathVariable ObjectId id, @RequestBody JournalV2 updateEntry) {
        JournalV2 oldEntry = journalServices.getEntryById(id).orElse(null);
        if (oldEntry != null) {
            oldEntry.setTitle(updateEntry.getTitle() != null && !updateEntry.getTitle().isEmpty() ? updateEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(updateEntry.getContent() != null && !updateEntry.getContent().isEmpty() ? updateEntry.getContent() : oldEntry.getContent());
            oldEntry.setDate(LocalDateTime.now());
        }
        journalServices.saveEntry(oldEntry);
        return oldEntry;    // we are modifying old entry
    }


}

/*
    controller => service => repository
*/