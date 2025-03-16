package com.whatwillieat.shopping_list.service;

import com.whatwillieat.shopping_list.model.ShoppingListItem;
import com.whatwillieat.shopping_list.repository.ShoppingListItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShoppingListItemService {
    private final ShoppingListItemRepository shoppingListItemRepository;

    @Autowired
    public ShoppingListItemService(ShoppingListItemRepository shoppingListItemRepository) {
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    public ShoppingListItem save(ShoppingListItem shoppingListItem) {
        return shoppingListItemRepository.save(shoppingListItem);
    }

    public void delete(UUID id) {
        ShoppingListItem item = getItemOrThrow(id);
        shoppingListItemRepository.delete(item);
    }

    public void softDelete(UUID id) {
        ShoppingListItem item = getItemOrThrow(id);
        item.setDeleted(true);
        shoppingListItemRepository.save(item);
    }

    public void softDelete(UUID listId, UUID itemId) {
        ShoppingListItem item = getItemOrThrow(itemId);

        // Validate that the item belongs to the correct list
        if (!item.getShoppingList().getId().equals(listId)) {
            throw new IllegalArgumentException("Item does not belong to the specified shopping list");
        }

        // Perform soft delete
        item.setDeleted(true);
        shoppingListItemRepository.save(item);
    }

    public void check(UUID id) {
        ShoppingListItem item = getItemOrThrow(id);
        item.setChecked(true);
        shoppingListItemRepository.save(item);
    }

    public void uncheck(UUID id) {
        ShoppingListItem item = getItemOrThrow(id);
        item.setChecked(true);
        shoppingListItemRepository.save(item);
    }

    private ShoppingListItem getItemOrThrow(UUID id) {
        return shoppingListItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list item not found"));
    }
}
