package com.sandesh.java.springboot.springBootTutorial.services;

import com.sandesh.java.springboot.springBootTutorial.entity.JournalV2;
import com.sandesh.java.springboot.springBootTutorial.entity.User;
import com.sandesh.java.springboot.springBootTutorial.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalServices {
    @Autowired
    private JournalRepository journalRepository;
    // field based dependency injection
    // it's an interface so, it's implementation will write here. we have pre-built methods MangoRepository

    @Autowired
    private UserServices userServices;

    @Transactional
    public void saveEntry(JournalV2 journalV2, String username) {
        try {
            User user = userServices.findUserByUsername(username);
            journalV2.setDate(LocalDateTime.now());
            JournalV2 saved = journalRepository.save(journalV2);  // save() is from spring data jpa. saving entry to journals collection.
            user.getJournalEntries().add(saved);    // add entry ref(_id) to user's journalEntries field
//            user.setUsername(null);
            userServices.saveUser(user);    // save the entry in users collection
        }catch (Exception e){
            System.out.println("exception => "+ e);
            throw new RuntimeException("an error occurred while saving journal entry", e);
        }
    }

    // this is for updating entry
    public void saveEntry(JournalV2 journalV2) {
       journalRepository.save(journalV2);  // save() is from spring data jpa. saving entry to journals collection.
    }

    public List<JournalV2> getAllEntries() {
        return journalRepository.findAll();    // findAll() is from spring data jpa
    }

    public Optional<JournalV2> getEntryById(ObjectId id) {
        return journalRepository.findById(id);
    }

    public void deleteEntryById(ObjectId id, String username) {
        User user = userServices.findUserByUsername(username);
        user.getJournalEntries().removeIf(x -> x.getId().equals(id));   // delete ref of journal from users collection as well.
        userServices.saveUser(user);
        journalRepository.deleteById(id);
    }

}

/*
    @Service => special type of @Component => good naming convention =>
     like @Component it is registered as spring beans means its object will be created by spring ioc container

     @Autowired
     add dependency of other class or method or interface
     we can inject dependencies on three types
     1. field based
     2. constructor based
     3. setter based
*/
