package com.whatwillieat.shopping_list.service;

import com.whatwillieat.shopping_list.dto.ShoppingListItemResponse;
import com.whatwillieat.shopping_list.dto.ShoppingListResponse;
import com.whatwillieat.shopping_list.model.ShoppingList;
import com.whatwillieat.shopping_list.repository.ShoppingListRepository;
import com.whatwillieat.shopping_list.dto.mapper.DtoMapper;
import com.whatwillieat.shopping_list.dto.ShoppingListRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShoppingListService {
    private final ShoppingListRepository shoppingListRepository;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public ShoppingListResponse create(ShoppingListRequest shoppingListRequest) {
        ShoppingList shoppingList = DtoMapper.fromShoppingListRequest(shoppingListRequest, null);
        shoppingListRepository.save(shoppingList);

        return ShoppingListResponse.builder()
                .id(shoppingList.getId())
                .name(shoppingList.getName())
                .description(shoppingList.getDescription())
                .ownerId(shoppingList.getOwnerId())
                .createdOn(shoppingList.getCreatedOn())
                .updatedOn(shoppingList.getUpdatedOn())
                .items(new ArrayList<>())
                .isDeleted(false)
                .build();
    }

    public ShoppingListResponse update(ShoppingListRequest shoppingListRequest) {
        ShoppingList existingShoppingList = shoppingListRepository.findById(shoppingListRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found"));

        ShoppingList updatedShoppingList = DtoMapper.fromShoppingListRequest(shoppingListRequest, existingShoppingList);
        shoppingListRepository.save(updatedShoppingList);

        List<ShoppingListItemResponse> itemResponses = existingShoppingList.getItems().stream().map(DtoMapper::toShoppingListItemResponse).collect(Collectors.toList());

        return ShoppingListResponse.builder()
                .id(updatedShoppingList.getId())
                .name(updatedShoppingList.getName())
                .description(updatedShoppingList.getDescription())
                .ownerId(existingShoppingList.getOwnerId())
                .createdOn(updatedShoppingList.getCreatedOn())
                .updatedOn(updatedShoppingList.getUpdatedOn())
                .items(itemResponses)
                .isDeleted(updatedShoppingList.isDeleted())
                .build();
    }

    public ShoppingListResponse getShoppingListById(UUID shoppingListId) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found"));

        List<ShoppingListItemResponse> itemResponses = shoppingList.getItems().stream().map(DtoMapper::toShoppingListItemResponse).collect(Collectors.toList());

        return ShoppingListResponse.builder()
                .id(shoppingList.getId())
                .name(shoppingList.getName())
                .description(shoppingList.getDescription())
                .ownerId(shoppingList.getOwnerId())
                .createdOn(shoppingList.getCreatedOn())
                .updatedOn(shoppingList.getUpdatedOn())
                .items(itemResponses)
                .isDeleted(shoppingList.isDeleted())
                .build();
    }

    public List<ShoppingListResponse> getShoppingListsByOwner(UUID ownerId) {
//        List<ShoppingList> shoppingLists = shoppingListRepository.findByOwnerIdAndIsDeletedFalseOrderByUpdatedOnDesc(ownerId);
        List<ShoppingList> shoppingLists = shoppingListRepository.findByOwnerIdOrderByUpdatedOnDesc(ownerId);

        return shoppingLists.stream().map(shoppingList ->  {

            List<ShoppingListItemResponse> itemResponses = shoppingList.getItems().stream().map(DtoMapper::toShoppingListItemResponse).collect(Collectors.toList());

            return ShoppingListResponse.builder()
                    .id(shoppingList.getId())
                    .name(shoppingList.getName())
                    .description(shoppingList.getDescription())
                    .ownerId(shoppingList.getOwnerId())
                    .createdOn(shoppingList.getCreatedOn())
                    .updatedOn(shoppingList.getUpdatedOn())
                    .items(itemResponses)
                    .isDeleted(shoppingList.isDeleted())
                    .build();
        }).collect(Collectors.toList());
    }

    public void softDelete(UUID id) {
        ShoppingList shoppingList = shoppingListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found"));

        shoppingList.setDeleted(true);
        shoppingListRepository.save(shoppingList);
    }

    public ShoppingListResponse undoSoftDelete(UUID id) {
        ShoppingList shoppingList = shoppingListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found"));

        shoppingList.setDeleted(false);
        shoppingListRepository.save(shoppingList);

        List<ShoppingListItemResponse> itemResponses = shoppingList.getItems().stream().map(DtoMapper::toShoppingListItemResponse).collect(Collectors.toList());

        return ShoppingListResponse.builder()
                .id(shoppingList.getId())
                .name(shoppingList.getName())
                .description(shoppingList.getDescription())
                .ownerId(shoppingList.getOwnerId())
                .createdOn(shoppingList.getCreatedOn())
                .updatedOn(shoppingList.getUpdatedOn())
                .items(itemResponses)
                .isDeleted(shoppingList.isDeleted())
                .build();
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
    public void delete(UUID shoppingListId) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found"));
        shoppingListRepository.delete(shoppingList);
    }

    // only called by chron job
    public void deleteByOwnerId(UUID ownerId) {
        shoppingListRepository.deleteByOwnerId(ownerId);
    }
}
