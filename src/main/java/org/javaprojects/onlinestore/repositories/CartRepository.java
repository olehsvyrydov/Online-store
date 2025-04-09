package org.javaprojects.onlinestore.repositories;


import org.javaprojects.onlinestore.entities.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;


public interface CartRepository extends CrudRepository<Cart, Long> {
    @Override
    @NonNull
    List<Cart> findAll();

    Optional<Cart> findByItem_Id(Long itemId);

    void deleteByItem_id(Long id);
}
