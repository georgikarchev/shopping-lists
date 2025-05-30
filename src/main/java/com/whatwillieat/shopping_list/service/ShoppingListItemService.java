package com.whatwillieat.shopping_list.service;

import com.whatwillieat.shopping_list.dto.ShoppingListItemResponse;
import com.whatwillieat.shopping_list.dto.ShoppingListResponse;
import com.whatwillieat.shopping_list.model.ShoppingList;
import com.whatwillieat.shopping_list.model.ShoppingListItem;
import com.whatwillieat.shopping_list.repository.ShoppingListItemRepository;
import com.whatwillieat.shopping_list.repository.ShoppingListRepository;
import com.whatwillieat.shopping_list.dto.ShoppingListItemRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ShoppingListItemService {
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final ShoppingListRepository shoppingListRepository;

    @Autowired
    public ShoppingListItemService(ShoppingListItemRepository shoppingListItemRepository, ShoppingListRepository shoppingListRepository) {
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    public ShoppingListItemResponse save(ShoppingListItemRequest shoppingListItemRequest) {

        Optional<ShoppingList> shoppingList = shoppingListRepository.findById(shoppingListItemRequest.getShoppingListId());

        if (shoppingList.isEmpty()) {
            throw new EntityNotFoundException("Shopping list not found");
        }

        ShoppingListItem shoppingListItem = shoppingListItemRepository.save(ShoppingListItem.builder()
                .name(shoppingListItemRequest.getName())
                .description(shoppingListItemRequest.getDescription())
                .shoppingList(shoppingList.get())
                .isChecked(Optional.ofNullable(shoppingListItemRequest).map(ShoppingListItemRequest::isChecked).orElse(false))
                .isDeleted(Optional.ofNullable(shoppingListItemRequest).map(ShoppingListItemRequest::isDeleted).orElse(false))
                .build());

        return ShoppingListItemResponse.builder()
                .id(shoppingListItem.getId())
                .name(shoppingListItem.getName())
                .description(shoppingListItem.getDescription())
                .isChecked(shoppingListItem.isChecked())
                .isDeleted(shoppingListItem.isDeleted())
                .build();
    }

    public void delete(UUID id) {
        ShoppingListItem item = getItemOrThrow(id);
        shoppingListItemRepository.delete(item);
    }

    public ShoppingListItemResponse undoSoftDelete(UUID shoppingListId, UUID shoppingListItemId) {
        ShoppingListItem item = getItemOrThrow(shoppingListItemId);

        // Validate that the item belongs to the correct list
        UUID itemShoppingList = item.getShoppingList().getId();
        if (!item.getShoppingList().getId().equals(shoppingListId)) {
            throw new IllegalArgumentException("Item does not belong to the specified shopping list");
        }

        item.setDeleted(false);
        shoppingListItemRepository.save(item);

        return ShoppingListItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isChecked(item.isChecked())
                .isDeleted(item.isDeleted())
                .build();
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

    public ShoppingListItemResponse check(UUID id) {
        ShoppingListItem item = getItemOrThrow(id);
        item.setChecked(true);
        shoppingListItemRepository.save(item);

        return ShoppingListItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isChecked(item.isChecked())
                .isDeleted(item.isDeleted())
                .build();
    }

    public ShoppingListItemResponse uncheck(UUID id) {
        ShoppingListItem item = getItemOrThrow(id);
        item.setChecked(false);
        shoppingListItemRepository.save(item);

        return ShoppingListItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isChecked(item.isChecked())
                .isDeleted(item.isDeleted())
                .build();
    }

    private ShoppingListItem getItemOrThrow(UUID id) {
        return shoppingListItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list item not found"));
    }
}
