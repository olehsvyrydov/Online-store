package org.javaprojects.onlinestore.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    private Long itemId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "item_id")
    private Item item;

    public Cart() {}

    public Cart(Item item) {
        this.item = item;
    }

    public Long getItemId() {
        return itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

