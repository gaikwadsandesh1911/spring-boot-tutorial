package com.sandesh.java.springboot.springBootTutorial.repository;

import com.sandesh.java.springboot.springBootTutorial.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    // created custom method which return User object based on provided username.
    User findByUsername(String username);

    void deleteByUsername(String username);
}
