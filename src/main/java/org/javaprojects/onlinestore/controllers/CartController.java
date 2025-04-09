package org.javaprojects.onlinestore.controllers;

import org.javaprojects.onlinestore.enums.Action;
import org.javaprojects.onlinestore.models.ItemModel;
import org.javaprojects.onlinestore.services.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is used to handle all requests related to the cart of items.
 * It contains methods to get all items in the basket and update items in the basket.
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    private final CatalogService catalogService;

    public CartController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /**
     * This method is used to get all items in the basket and display them on the cart page.
     * @param model model
     * @return cart.html
     */
    @GetMapping("/items")
    public String getItemsInBasket(
            Model model) {
        List<ItemModel> productListInBasket = catalogService.getItemsInBasket();
        double totalPrice = productListInBasket.stream()
                .mapToDouble(itemModel -> itemModel.getPrice().doubleValue() * itemModel.getCount())
                .sum();
        boolean isEmpty = productListInBasket.isEmpty();

        model.addAttribute("items", productListInBasket);
        model.addAttribute("total", totalPrice);
        model.addAttribute("empty", isEmpty);
        return "cart";
    }

    /**
     * This method is used to update the count of items in the basket.
     * @param id id of the item
     * @param action action to be performed on the item
     * @return redirect to the cart page
     */
    @PostMapping("/items/{id}")
    public String updateItemCountInBasket(
            @PathVariable("id") Long id,
            @RequestParam("action") Action action) {
        catalogService.updateCountInBasket(id, action);
        return "redirect:/cart/items";
    }
}
