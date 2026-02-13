package org.javaprojects.onlinestore.repositories;

import org.javaprojects.onlinestore.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
