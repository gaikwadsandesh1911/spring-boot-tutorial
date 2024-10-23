package com.sandesh.java.springboot.springBootTutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement    // it's for @Transactional method.
public class TransactionConfig {
    @Bean
    public PlatformTransactionManager platformTransactionManager(MongoDatabaseFactory dbFactory){
        return  new MongoTransactionManager(dbFactory);
    }
}

/*
    @EnableTransactionManagement => it search for @Transactional annotation method. and spring boot create
   TransactionalContext(container) corresponding to each method.

    method with @Transactional annotation, whatever we do will treat as single operation
    if one operation fail, all should treat as fail, and any operation  succeed will roll back it.

    PlatformTransactionManager interface do all this commit, rollback operation with
     it's implementation class MongoTransactionManager.

    we have to configure it to create bean using method with @Bean annotation on method.

    MongoDatabaseFactory help us to make connection with database. and session means TransactionContext.

    ** inside mongodb database replica is necessary Transaction happen.
*/