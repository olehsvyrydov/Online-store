package org.javaprojects.onlinestore.controllers;

import org.javaprojects.onlinestore.entities.Item;
import org.javaprojects.onlinestore.repositories.CartRepository;
import org.javaprojects.onlinestore.repositories.ItemsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ItemsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ItemsRepository itemsRepository;
    @MockitoBean
    private CartRepository cartRepository;
    private static final Item ITEM = new Item(1L, "Test Title", "Test Description", new BigDecimal("19.99"), "test-path.jpg", 1);

    @BeforeEach
    void setUp() {
        Mockito.when(itemsRepository.findById(anyLong())).thenReturn(Optional.of(ITEM));
    }

    @Test
    void getItemById() throws Exception {
        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("item"))
                .andExpect(model().attributeExists("itemModel"));
    }

    @Test
    void updateItemCountInBasket_plusAction() throws Exception {

        mockMvc.perform(post("/items/1")
                        .queryParam("action", "plus"))
                .andExpect(redirectedUrl("/items/1"));
    }

    @Test
    void updateItemCountInBasket_minusAction() throws Exception {
        mockMvc.perform(post("/items/1")
                        .queryParam("action", "minus"))
                .andExpect(redirectedUrl("/items/1"));
    }

    @Test
    void updateItemCountInBasket_deleteAction() throws Exception {
        mockMvc.perform(post("/items/1")
                        .queryParam("action", "delete"))
                .andExpect(redirectedUrl("/items/1"));
    }
}