package org.javaprojects.onlinestore.controllers;

import org.javaprojects.onlinestore.converters.StringToActionConverter;
import org.javaprojects.onlinestore.entities.Item;
import org.javaprojects.onlinestore.models.ItemModel;
import org.javaprojects.onlinestore.models.OrderModel;
import org.javaprojects.onlinestore.services.CatalogService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = OrdersController.class)
@ContextConfiguration(classes = {OrdersController.class, StringToActionConverter.class})
class OrdersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CatalogService catalogService;

    @Test
    void getOrders() throws Exception {
        Item item1 = new Item(1L, "Test Title1", "Test Description1", new BigDecimal("19.99"), "test-path1.jpg", 1);
        Item item2 = new Item(2L, "Test Title2", "Test Description2", new BigDecimal("29.99"), "test-path2.jpg", 2);
        OrderModel orderModel = new OrderModel(1L, List.of(new ItemModel(item1, item1.getCount()), new ItemModel(item2, item1.getCount())), new BigDecimal("79.97"));
        OrderModel orderModel2 = new OrderModel(1L, List.of(new ItemModel(item1, item1.getCount()), new ItemModel(item2, item2.getCount())), new BigDecimal("79.97"));
        List<OrderModel> orderModelList = List.of(orderModel, orderModel2);
        Mockito.when(catalogService.findAllOrders()).thenReturn(orderModelList);
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attribute("orders", orderModelList));
    }

    @Test
    void getOrderById() throws Exception {
        Item item1 = new Item(1L, "Test Title1", "Test Description1", new BigDecimal("19.99"), "test-path1.jpg", 1);
        Item item2 = new Item(2L, "Test Title2", "Test Description2", new BigDecimal("29.99"), "test-path2.jpg", 2);
        OrderModel orderModel = new OrderModel(1L, List.of(new ItemModel(item1, item1.getCount()), new ItemModel(item2, item2.getCount())), new BigDecimal("79.97"));
        Mockito.when(catalogService.getOrderById(anyLong())).thenReturn(orderModel);
        mockMvc.perform(get("/orders/1")
                        .queryParam("newOrder", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attribute("order", orderModel));
    }

    @Test
    void buy() throws Exception {
        Mockito.when(catalogService.buyItemsInBasket()).thenReturn(1L);
        mockMvc.perform(post("/buy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/1?newOrder=true"));
    }
}