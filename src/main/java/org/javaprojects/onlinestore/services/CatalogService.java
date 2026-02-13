package org.javaprojects.onlinestore.services;

import org.javaprojects.onlinestore.entities.Cart;
import org.javaprojects.onlinestore.entities.Item;
import org.javaprojects.onlinestore.entities.Order;
import org.javaprojects.onlinestore.entities.OrderItem;
import org.javaprojects.onlinestore.enums.Action;
import org.javaprojects.onlinestore.enums.Sorting;
import org.javaprojects.onlinestore.models.AllItemsModel;
import org.javaprojects.onlinestore.models.ItemModel;
import org.javaprojects.onlinestore.models.OrderModel;
import org.javaprojects.onlinestore.models.Paging;
import org.javaprojects.onlinestore.repositories.CartRepository;
import org.javaprojects.onlinestore.repositories.ItemsRepository;
import org.javaprojects.onlinestore.repositories.OrdersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    private final ItemsRepository itemRepository;
    private final OrdersRepository ordersRepository;
    private final CartRepository cartRepository;
    private final ItemsRepository itemsRepository;

    public CatalogService(ItemsRepository itemRepository, OrdersRepository ordersRepository,
                          CartRepository cartRepository, ItemsRepository itemsRepository) {
        this.itemRepository = itemRepository;
        this.ordersRepository = ordersRepository;
        this.cartRepository = cartRepository;
        this.itemsRepository = itemsRepository;
    }

    public AllItemsModel findAllItems(int pageNumber, int pageSize, String searchString, Sorting sorting) {
        Sort sorted = switch (sorting) {
            case NO -> Sort.unsorted();
            case PRICE -> Sort.by("price").ascending();
            case ALPHA -> Sort.by("title").ascending();
        };
        Page<Item> page;
        if (searchString.isEmpty()) {
            page = itemRepository.findAll(PageRequest.of(pageNumber, pageSize, sorted));
        } else {
            page = itemRepository
                    .findBySearchString(searchString, PageRequest.of(pageNumber, pageSize, sorted));
        }

        List<ItemModel> result = page.get()
                .map(i -> new ItemModel(i.getId(),i.getTitle(), i.getDescription(), i.getPrice(), i.getImgPath(), i.getCount()))
                .toList();


        return new AllItemsModel(result, new Paging(page.getNumber(), page.getSize(), page.hasNext(), page.hasPrevious()));
    }

    public ItemModel getItemById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new IllegalStateException("Item not found"));
        return new ItemModel(item.getId(),item.getTitle(), item.getDescription(), item.getPrice(), item.getImgPath(), item.getCount());
    }

    public void decrementQuantity(Long itemId) {
        Cart cart = cartRepository.findByItem_Id(itemId).orElse(null);
        if (cart == null) {
            return;
        }
        Item item = cart.getItem();
        int updatedCount = item.getCount();
        if (updatedCount == 1) {
            cartRepository.delete(cart);
            item.setCount(0);
        }
        item.decrementCount();
        cartRepository.save(cart);
    }

    public void incrementQuantity(Long itemId) {
        Cart cart = cartRepository.findByItem_Id(itemId).orElseGet(
                 () -> {
                    Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalStateException("Item not found"));
                    Cart cartItem = new Cart(item);
                    cartRepository.save(cartItem);
                    return cartItem;
                });
            cart.getItem().incrementCount();
            cartRepository.save(cart);
    }

    public void deleteItemFromBasket(Long itemId) {
        cartRepository.deleteByItem_id(itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalStateException("Item not found"));
        item.setCount(0);
    }

    public List<ItemModel> getItemsInBasket() {
        List<Cart> itemsList = cartRepository.findAll();
        return itemsList.stream()
                .map(CatalogService::mapCartToItemModel)
                .toList();
    }

    private static ItemModel mapCartToItemModel(Cart cart) {
        Item item = cart.getItem();
        return new ItemModel(item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getPrice(),
                item.getImgPath(),
                item.getCount());
    }

    @Transactional
    public void updateCountInBasket(Long id, Action action) {
        switch (action) {
            case Action.PLUS -> this.incrementQuantity(id);
            case Action.MINUS -> this.decrementQuantity(id);
            case Action.DELETE -> this.deleteItemFromBasket(id);
            default -> throw new IllegalStateException("Invalid action: " + action);
        };
    }

    public List<OrderModel> findAllOrders() {
        List<Order> orders = ordersRepository.findAll();
        return orders.stream()
                .map(order -> {
                            List<ItemModel> itemModels = order.getOrderItems().stream()
                                    .map(orderItem -> new ItemModel(orderItem.getItem(), orderItem.getQuantity())).toList();
                            return new OrderModel(order.getId(), itemModels, order.getTotal());
                        }
                )
                .toList();
    }

    public OrderModel getOrderById(Long id) {
        Order order = ordersRepository.findById(id).orElseThrow(() -> new IllegalStateException("Order not found"));
        List<ItemModel> itemModels = order.getOrderItems().stream()
                .map(orderItem -> new ItemModel(orderItem.getItem(), orderItem.getQuantity()))
                .toList();
        return new OrderModel(order.getId(), itemModels, order.getTotal());
    }

    @Transactional
    public Long buyItemsInBasket() {
        List<Cart> cartList = cartRepository.findAll();
        Order order = new Order();
        Set<OrderItem> orderItemSet = cartList.stream()
                .map(cart -> new OrderItem(order, cart.getItem(), cart.getItem().getCount()))
                .collect(Collectors.toSet());

        order.setOrderItems(orderItemSet);
        order.setTotal(cartList.stream()
                .map(cart -> cart.getItem().getPrice().multiply(BigDecimal.valueOf(cart.getItem().getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Order newOrder = ordersRepository.save(order);
        cartRepository.deleteAll(cartList);
        for (Cart cart : cartList) {
            Item item = cart.getItem();
            item.setCount(0);
            itemsRepository.save(item);
        }
        return newOrder.getId();
    }
}
