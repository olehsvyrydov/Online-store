package org.javaprojects.onlinestore.controllers;

import org.javaprojects.onlinestore.services.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This class is used to handle all requests related to the orders of items.
 * It contains methods to get all orders, get order by id and buy items in the basket.
 */
@Controller
public class OrdersController {
    private final CatalogService catalogService;

    public OrdersController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /**
     * This method is used to get all orders from the database and display them on the orders page.
     * @param model model
     * @return orders.html
     */
    @GetMapping("/orders")
    public String getOrders(Model model) {
        model.addAttribute("orders", catalogService.findAllOrders());
        return "orders";
    }

    /**
     * This method is used to get order by id from the database and display it on the order page.
     * @param id id of the order
     * @param isNewOrder boolean value to check if the order is new
     * @param model model
     * @return order.html
     */
    @GetMapping("/orders/{id}")
    public String getOrderById(
            @PathVariable("id") Long id,
            @RequestParam(value = "newOrder", defaultValue = "false") Boolean isNewOrder,
            Model model
    ) {
        model.addAttribute("order", catalogService.getOrderById(id));
        return "order";
    }

    /**
     * Buying items in the basket and clearing it
     * @return redirect to the order page
     */
    @PostMapping("/buy")
    public String buy() {
        Long id = catalogService.buyItemsInBasket();
        return "redirect:/orders/" + id + "?newOrder=true";
    }
}
