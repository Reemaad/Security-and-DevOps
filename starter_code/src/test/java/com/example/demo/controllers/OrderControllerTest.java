package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private OrderRepository orderRepository  = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private Item item = new Item();
    User user = new User();


    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);


        List<Item> items = new ArrayList<>();
        List<UserOrder> userOrders = new ArrayList<>();

        item.setId(2L);
        item.setName("test item");
        item.setPrice(BigDecimal.valueOf(2.95));
        item.setDescription("this item for testing");

        items.add(item);


        user.setId(0);
        user.setUsername("test1");
        user.setCart(new Cart());
        user.getCart().setItems(items);
        user.getCart().setUser(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setUser(user);
        userOrder.setItems(items);
        userOrders.add(userOrder);

        when(orderRepository.findByUser(user)).thenReturn(userOrders);


    }

    @Test
    public void submit_order() {

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> userOrders = response.getBody();

        assertNotNull(userOrders);
        assertEquals(String.valueOf(1L),userOrders.get(0).getId().toString());
    }


}
