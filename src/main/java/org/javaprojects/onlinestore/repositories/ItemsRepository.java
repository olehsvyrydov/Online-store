package org.javaprojects.onlinestore.repositories;

import org.javaprojects.onlinestore.entities.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemsRepository extends ListPagingAndSortingRepository<Item, Long>
{
    Optional<Item> findById(Long id);
    @Query("""
            SELECT i FROM Item i
            WHERE LOWER(i.title) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Page<Item> findBySearchString(@Param("search") String search, Pageable pageable);
    void save(Item item);
}