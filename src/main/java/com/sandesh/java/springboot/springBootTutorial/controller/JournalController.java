package com.sandesh.java.springboot.springBootTutorial.controller;

import com.sandesh.java.springboot.springBootTutorial.entity.Journal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalController {

    // we don't have database integrated so.
    private final Map<Long, Journal> journalEntries = new HashMap<>();

    @PostMapping
    public String createNew(@RequestBody Journal newEntry) {
        journalEntries.put(newEntry.getId(), newEntry);
        return newEntry.getTitle();
    }

    @GetMapping
    public List<Journal> getAll() {
        return new ArrayList<>(journalEntries.values());
    }

    @GetMapping("/id/{myId}")
    public Journal getById(@PathVariable Long myId) {
        return journalEntries.get(myId);
    }

    @DeleteMapping("/id/{myId}")
    public String deleteById(@PathVariable Long myId) {
        journalEntries.remove(myId);
        return "deleted successfully";
    }

    @PutMapping("/id/{myId}")
    public Journal updateById(@PathVariable Long myId, @RequestBody Journal myEntry) {
        journalEntries.put(myId, myEntry);
        return journalEntries.get(myId);
    }

}
