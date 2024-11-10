package com.sandesh.java.springboot.springBootTutorial.services;

import com.sandesh.java.springboot.springBootTutorial.entity.JournalV2;
import com.sandesh.java.springboot.springBootTutorial.entity.User;
import com.sandesh.java.springboot.springBootTutorial.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class JournalServices {
    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserServices userServices;


    @Transactional
    public void saveEntry(JournalV2 journalV2, String username) {
        try {
            journalV2.setDate(LocalDateTime.now());
            JournalV2 saved = journalRepository.save(journalV2);  // save() is from spring data jpa. saving entry to journals collection.
            User user = userServices.findUserByUsername(username);
            user.getJournalEntries().add(saved);    // add entry ref(_id) to user's journalEntries field
//            user.setUsername(null);
            userServices.saveUser(user);    // save the entry in users collection
        } catch (Exception e) {
            System.out.println("exception => " + e);
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

    @Transactional
    public boolean deleteEntryById(ObjectId id, String username) {
        try {
            // we have to delete ref of journal from users collection also if id is present in users journalEntries field
            User user = userServices.findUserByUsername(username);
            boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userServices.saveUser(user);
                journalRepository.deleteById(id);
            }
            return  removed;

        } catch (Exception e) {
            System.out.println("Delete entry by id exception " + e.getMessage());
            throw new RuntimeException("An error occurred during delete journal by id");
        }
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

     @Transactional => annotation

     Methods annotated with @Transactional are wrapped in a transactional context,
     meaning all operations within the method are part of a single transaction.
     If any operation fails, the entire transaction is rolled back.

     @EnableTransactionManagement is essential for enabling the use of @Transactional.
      Without it, @Transactional annotations won’t work.

      in config package we have made class TransactionConfig where we used @EnableTransactionManagement
*/
