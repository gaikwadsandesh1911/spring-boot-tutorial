package com.sandesh.java.springboot.springBootTutorial.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
@Document(collection = "journals")
@Data
@NoArgsConstructor // used for de-serialization means json to pojo
public class JournalV2 {
    @Id
    private ObjectId id;    // ObjectId is bson from mangoDB
    @NonNull
    private String title;
    @NonNull
    private String content;
    private LocalDateTime date;

    // Getters and Setters.
    // we have lombok annotations for Getters and Setters

}

