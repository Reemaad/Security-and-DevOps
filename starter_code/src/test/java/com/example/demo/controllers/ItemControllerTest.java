package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private Item item = new Item();

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        List<Item> items = new ArrayList<>();

        item.setId(2L);
        item.setName("test item");
        item.setPrice(BigDecimal.valueOf(2.95));
        item.setDescription("this item for testing");

        items.add(item);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.findByName(item.getName())).thenReturn(items);
        when(itemRepository.findAll()).thenReturn(items);


    }

    @Test
    public void get_items() {

        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();

        assertNotNull(items);
        assertFalse(items.isEmpty());
    }

    @Test
    public void get_item_by_id() {

        final ResponseEntity<Item> response = itemController.getItemById(2L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item item = response.getBody();

        assertNotNull(item);
        assertEquals(this.item.getId(),item.getId());
    }

    @Test
    public void get_items_by_name() {

        final ResponseEntity<List<Item>> response = itemController.getItemsByName(item.getName());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();

        assertNotNull(items);
        assertFalse(items.isEmpty());
    }


}

