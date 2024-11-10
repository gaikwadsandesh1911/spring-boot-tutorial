package com.sandesh.java.springboot.springBootTutorial.services;

import com.sandesh.java.springboot.springBootTutorial.entity.User;
import com.sandesh.java.springboot.springBootTutorial.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

//@SpringBootTest
class CustomUserDetailsServiceTest {

//    @Autowired
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

//    @MockBean
    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
//        initializes the annotated mocks (@Mock and @InjectMocks).
    }


    @Test
    void loadUserByUsername() {
        when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(User.builder().username("sandesh").password("ksjhkadkjh").roles(new ArrayList<>()).build());
        UserDetails user = customUserDetailsService.loadUserByUsername("sandesh");
        assertNotNull(user);
    }

}


/*

    @SpringBootTest: it loads the entire application context for integration testing.

    @Mock: Mocks UserRepository, avoiding the need for an actual database connection.

    @InjectMocks tells Mockito to inject mock dependencies (e.g., userRepository)
    into the CustomUserDetailsService instance automatically.

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    when we @Autowired means we create Bean of Component which start ApplicationContext,
    so it also need @SpringBootTest and @MockBean

    we don't want to fetch data from db,
    also by starting ApplicationContext means load all the components of application.
    because it takes lot of time,

    we want to mock(fake) the data to test.


    // here when userRepository's findByUsername() method is called we return fake object.

    when(userRepository.findByUsername(ArgumentMatchers.anyString()))
    .thenReturn(User.builder().username("sandesh").password("ksjhkadkjh").roles(new ArrayList<>()).build());



    // now we don't have instance of userRepository so call this method
    which runs BeforeEach test in this class.

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        // initialize all the mocks of this claass
    }

*/
