package org.javaprojects.onlinestore.repositories;

import org.javaprojects.onlinestore.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Order, Long> {

}
