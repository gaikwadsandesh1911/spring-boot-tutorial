package com.sandesh.java.springboot.springBootTutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class SpringBootTutorialApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootTutorialApplication.class, args);
    }
}



//@SpringBootApplication
//public class SpringBootTutorialApplication {
//    public static void main(String[] args) {
//
//        ConfigurableApplicationContext context = SpringApplication.run(SpringBootTutorialApplication.class, args);
//        ConfigurableEnvironment environment = context.getEnvironment();
//        System.out.println(environment.getActiveProfiles()[0]);     // get active profile. we set in application.yml
//    }
//
//}

