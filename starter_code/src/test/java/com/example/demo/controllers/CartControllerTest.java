package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private Item item = new Item();

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        item.setId(2L);
        item.setName("test item");
        item.setPrice(BigDecimal.valueOf(2.95));
        item.setDescription("this item for testing");
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));


    }

    @Test
    public void add_to_cart() {
        User user = new User();
        user.setId(0);
        user.setUsername("test1");
        user.setCart(new Cart());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test1");
        modifyCartRequest.setItemId(2);
        modifyCartRequest.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();


        assertNotNull(cart);
        assertEquals(Optional.of(modifyCartRequest.getItemId()), Optional.of(cart.getItems().get(0).getId()));
    }

    @Test
    public void remove_from_cart() {
        User user = new User();
        user.setId(0);
        user.setUsername("test1");
        user.setCart(new Cart());
        user.getCart().addItem(item);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test1");
        modifyCartRequest.setItemId(2);
        modifyCartRequest.setQuantity(1);


        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();


        assertNotNull(cart);
        assertTrue(cart.getItems().size() == 0);

    }
}
