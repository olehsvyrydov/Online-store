package org.javaprojects.onlinestore.controllers;

import org.javaprojects.onlinestore.entities.Cart;
import org.javaprojects.onlinestore.entities.Item;
import org.javaprojects.onlinestore.repositories.CartRepository;
import org.javaprojects.onlinestore.repositories.ItemsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CartRepository cartRepository;
    @MockitoBean
    private ItemsRepository itemRepository;

    @BeforeEach
    void setUp() {
        Item item1 = new Item(1L, "Test Title1", "Test Description1", new BigDecimal("19.99"), "test-path1.jpg", 1);
        Item item2 = new Item(2L, "Test Title2", "Test Description2", new BigDecimal("29.99"), "test-path2.jpg", 2);

        List<Cart> cartList = List.of(
                new Cart(item1),
                new Cart(item2)
        );

        when(cartRepository.findAll()).thenReturn(cartList);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
    }

    @Test
    void getItemsInBasket() throws Exception {

        mockMvc.perform(get("/cart/items"))
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attribute("total", 79.97))
                .andExpect(model().attribute("empty", false));
    }

    @Test
    void updateItemCountInBasket_plusAction() throws Exception {
        mockMvc.perform(post("/cart/items/1")
                        .queryParam("action", "plus"))
                .andExpect(redirectedUrl("/cart/items"));
    }

    @Test
    void updateItemCountInBasket_minusAction() throws Exception {
        mockMvc.perform(post("/cart/items/1")
                        .queryParam("action", "minus"))
                .andExpect(redirectedUrl("/cart/items"));
    }

    @Test
    void updateItemCountInBasket_deleteAction() throws Exception {
        mockMvc.perform(post("/cart/items/1")
                        .queryParam("action", "delete"))
                .andExpect(redirectedUrl("/cart/items"));
        verify(cartRepository).deleteByItem_id(1L);
    }
}