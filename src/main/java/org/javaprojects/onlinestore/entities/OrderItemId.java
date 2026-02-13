package org.javaprojects.onlinestore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderItemId implements Serializable {
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "item_id")
    private Long itemId;

    public OrderItemId() {
    }

    public OrderItemId(Long orderId, Long itemId) {
        this.orderId = orderId;
        this.itemId = itemId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof OrderItemId that) {
            return Objects.equals(getOrderId(), that.getOrderId())
                && Objects.equals(getItemId(), that.getItemId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getItemId());
    }
}
