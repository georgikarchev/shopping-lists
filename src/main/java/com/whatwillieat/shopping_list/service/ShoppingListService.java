package com.whatwillieat.shopping_list.service;

import com.whatwillieat.shopping_list.model.ShoppingList;
import com.whatwillieat.shopping_list.repository.ShoppingListRepository;
import com.whatwillieat.shopping_list.service.mapper.DtoMapper;
import com.whatwillieat.shopping_list.web.dto.ShoppingListRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ShoppingListService {
    private final ShoppingListRepository shoppingListRepository;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public ShoppingList create(ShoppingListRequest shoppingListRequest) {
        ShoppingList shoppingList = DtoMapper.fromShoppingListRequest(shoppingListRequest, null);
        return shoppingListRepository.save(shoppingList);
    }

    public ShoppingList update(ShoppingListRequest shoppingListRequest) {
        ShoppingList existingShoppingList = shoppingListRepository.findById(shoppingListRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found"));

        ShoppingList updatedShoppingList = DtoMapper.fromShoppingListRequest(shoppingListRequest, existingShoppingList);
        return shoppingListRepository.save(updatedShoppingList);
    }

    public ShoppingList getShoppingListById(UUID shoppingListId) {
        return shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found"));
    }

    public List<ShoppingList> getShoppingListsByOwner(UUID ownerId) {
        return shoppingListRepository.findByOwnerIdAndIsDeletedFalseOrderByUpdatedOnDesc(ownerId);
    }

    public void softDelete(UUID id) {
        ShoppingList shoppingList = shoppingListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found"));

        shoppingList.setDeleted(true);
        shoppingListRepository.save(shoppingList);
    }

    public void softDeleteShoppingListsByOwnerId(UUID ownerId) {
        // Step 1: Find all shopping lists belonging to the owner
        List<ShoppingList> shoppingLists = shoppingListRepository.findByOwnerIdAndIsDeletedFalse(ownerId);

        // Step 2: Update the isDeleted flag to true for each shopping list
        for (ShoppingList shoppingList : shoppingLists) {
            shoppingList.setDeleted(true);  // Set the 'isDeleted' flag to true
        }

        // Step 3: Save the updated shopping lists back to the repository
        shoppingListRepository.saveAll(shoppingLists);
    }


    // only called by chron job
    public void delete(UUID id) {
        ShoppingList list = getShoppingListById(id);
        shoppingListRepository.delete(list);
    }

    // only called by chron job
    public void deleteByOwnerId(UUID ownerId) {
        shoppingListRepository.deleteByOwnerId(ownerId);
    }
}
