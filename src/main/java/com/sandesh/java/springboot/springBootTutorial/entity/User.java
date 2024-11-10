package com.sandesh.java.springboot.springBootTutorial.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;

    /* indexing is not done automatically we have to set property on application.properties file
    auto-index-creation: true*/
    @NonNull
    @Indexed(unique = true)
    private String username;
    @NonNull
    private String password;

    private List<String> roles;

    @DBRef
    private List<JournalV2> journalEntries = new ArrayList<>();
    // @DBRef holding reference of JournalV2 inside User (journals _id  inside users journalEntries field;)

    // so, on users collections, inside journalEntries field we have
    // ObjectId's of journal created by this user.
}

/*
    @NonNull => is from lombok,
     When you apply @NonNull to a field, Lombok generates a null-check for this field inside the constructor,
     it ensures that no null value can be passed during object creation

     if there is null values it throws an exception.

     public User(String name) {
        if (name == null) {
        throw new NullPointerException("name is marked @NonNull but is null");
     }
        this.name = name;

     we can apply @NonNull on methods so method can not return null value
*/

/*
@Indexed(unique = true) // indexing is not done automatically
we have to set property on application.properties file...
        spring.data.mongodb.auto-index-creation=true
*/