package com.whatwillieat.shopping_list.repository;

import com.whatwillieat.shopping_list.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, UUID> {

    List<ShoppingList> findByOwnerIdAndIsDeletedFalseOrderByUpdatedOnDesc(UUID ownerId);

    void deleteByOwnerId(UUID ownerId);

    List<ShoppingList> findByOwnerIdAndIsDeletedFalse(UUID ownerId);
}
