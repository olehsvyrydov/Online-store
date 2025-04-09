package org.javaprojects.onlinestore.services;

import org.javaprojects.onlinestore.entities.Item;
import org.javaprojects.onlinestore.repositories.ItemsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@Sql(statements = {
        """
        INSERT INTO items (title, description, price, img_path)
        VALUES ('Smartphone', 'Latest model smartphone with advanced features.', 699.99, '/images/smartphone.jpg');
        """,
        """
        INSERT INTO items (title, description, price, img_path)
        VALUES ('Laptop', 'High-performance laptop suitable for gaming and work.', 1199.50, '/images/laptop.jpg');
        """,
        """
        INSERT INTO items (title, description, price, img_path)
        VALUES ('Headphones', 'Noise-cancelling over-ear headphones with superior sound quality.', 199.95, '/images/headphones.jpg');
        """,
        """
        INSERT INTO items (title, description, price, img_path)
        VALUES ('Smartwatch', 'Feature-packed smartwatch with fitness tracking capabilities.', 249.99, '/images/smartwatch.jpg');
        """,
        """
        INSERT INTO items (title, description, price, img_path)
        VALUES ('Camera', 'Digital camera with high resolution and optical zoom.', 449.00, '/images/camera.jpg');
        """
})
class CatalogServiceTest {

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private CatalogService catalogService;

    @Test
    void getItemsInBasket() {
        Page<Item> itemPage = itemsRepository.findBySearchString("laptop", PageRequest.of(0, 10));
        Item item = itemPage.getContent().getFirst();
        assertEquals("Laptop", item.getTitle());
        assertEquals(1199.50, item.getPrice().doubleValue());
    }

    @Test
    void buyItemsInBasket() {
        Page<Item> smartphonePage = itemsRepository.findBySearchString("Smartphone", PageRequest.of(0, 10));
        Page<Item> laptopPage = itemsRepository.findBySearchString("laptop", PageRequest.of(0, 10));
        Item smartphone = smartphonePage.getContent().getFirst();
        Item laptop = laptopPage.getContent().getFirst();

        catalogService.incrementQuantity(smartphone.getId());
        catalogService.incrementQuantity(laptop.getId());
        catalogService.incrementQuantity(laptop.getId());
        Long id = catalogService.buyItemsInBasket();
        assertEquals(3098.99, catalogService.getOrderById(id).totalSum().doubleValue());
    }
}