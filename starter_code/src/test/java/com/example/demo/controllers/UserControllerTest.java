package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private  UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController,"userRepository", userRepository);
        TestUtils.injectObjects(userController,"cartRepository", cartRepository);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception{
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();


        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }

    @Test
    public void find_user_by_id() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test2");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        //Create user
        final ResponseEntity<User> createdUserResponse = userController.createUser(createUserRequest);
        assertNotNull(createdUserResponse);
        assertEquals(200, createdUserResponse.getStatusCodeValue());
        User user = createdUserResponse.getBody();
        assertNotNull(user);

        //Get user by id
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        final ResponseEntity<User> findByIdResponse = userController.findById(user.getId());
        assertNotNull(findByIdResponse);
        assertEquals(findByIdResponse.getBody().getId(), user.getId());
    }

    @Test
    public void find_user_by_username() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test3");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        //Create user
        final ResponseEntity<User> createdUserResponse = userController.createUser(createUserRequest);
        assertNotNull(createdUserResponse);
        assertEquals(200, createdUserResponse.getStatusCodeValue());
        User user = createdUserResponse.getBody();
        assertNotNull(user);

        //Get user by username
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        final ResponseEntity<User> findByUsernameResponse = userController.findByUserName(user.getUsername());
        assertNotNull(findByUsernameResponse);
        assertEquals(findByUsernameResponse.getBody().getId(), user.getId());
    }
}
