package org.javaprojects.onlinestore.controllers;

import org.javaprojects.onlinestore.converters.StringToActionConverter;
import org.javaprojects.onlinestore.enums.Sorting;
import org.javaprojects.onlinestore.models.AllItemsModel;
import org.javaprojects.onlinestore.models.ItemModel;
import org.javaprojects.onlinestore.models.Paging;
import org.javaprojects.onlinestore.services.CatalogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = CatalogController.class)
@ContextConfiguration(classes = {CatalogController.class, StringToActionConverter.class})
class CatalogControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CatalogService catalogService;


    @Test
    void getMainPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main/items"));
    }

    @Test
    void getAllProductsWithDefaultParameters() throws Exception {
        ItemModel item = new ItemModel(1L, "Test Title", "Test Description", new BigDecimal("19.99"), "test-path.jpg", 0);
        List<ItemModel> itemList = Collections.singletonList(item);
        AllItemsModel allItemsPaging = new AllItemsModel(itemList, new Paging(1, 10, true, false));
        Mockito.when(catalogService.findAllItems(anyInt(), anyInt(), anyString(), any(Sorting.class))).thenReturn(allItemsPaging);
        mockMvc.perform(get("/main/items")
                        .queryParam("search", "")
                        .queryParam("sort", "NO")
                        .queryParam("pageSize", "10")
                        .queryParam("pageNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attribute("items", allItemsPaging.getProductList()))
                .andExpect(model().attribute("paging", allItemsPaging.getPaging()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"plus","minus","delete"})
    void updateItemsCountInBasket_PlusItem(String action) throws Exception {
        ItemModel item = new ItemModel(1L, "Test Title", "Test Description", new BigDecimal("19.99"), "test-path.jpg", 0);
        Mockito.when(catalogService.getItemById(anyLong())).thenReturn(item);
        mockMvc.perform(post("/main/items/1")
                        .queryParam("action", action))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main/items"));
    }
}