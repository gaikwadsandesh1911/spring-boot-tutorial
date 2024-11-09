package com.sandesh.java.springboot.springBootTutorial.services;

import com.sandesh.java.springboot.springBootTutorial.entity.User;
import com.sandesh.java.springboot.springBootTutorial.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class UserServicesTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testAdd(){
        assertEquals(4, 2+2);
    }

//    @Test
//    public void testFindByUsername(){
//        User user = userRepository.findByUsername("sandesh");
//        assertFalse(user.getJournalEntries().isEmpty());
//    }

    @ParameterizedTest
    @CsvSource({
            "1,3,3",
            "2,2,4"
    })
    public void testMul(int a, int b, int expected){
        assertEquals(expected, a * b);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "subodh",
            "shubham"
    })
    public void testFindByUsername(String name){
        User user = userRepository.findByUsername(name);
        assertFalse(user.getUsername().isEmpty());
    }
}
