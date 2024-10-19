package com.sandesh.java.springboot.springBootTutorial.repository;
import com.sandesh.java.springboot.springBootTutorial.entity.JournalV2;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalRepository extends MongoRepository<JournalV2, ObjectId> {
}

/*
    for sql database we have JpaRepository interface
    for mongodb we have MangoRepository interface
    both interfaces extend other  interfaces as well. so we can use

    those method as well. to implement business logic in service layer.

    MongoRepository<Pojo, id type of Pojo>

 */
