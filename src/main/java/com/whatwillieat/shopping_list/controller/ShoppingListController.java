package com.whatwillieat.shopping_list.controller;

import com.whatwillieat.shopping_list.dto.ShoppingListItemResponse;
import com.whatwillieat.shopping_list.dto.ShoppingListResponse;
import com.whatwillieat.shopping_list.model.ShoppingList;
import com.whatwillieat.shopping_list.model.ShoppingListItem;
import com.whatwillieat.shopping_list.service.ShoppingListItemService;
import com.whatwillieat.shopping_list.service.ShoppingListService;
import com.whatwillieat.shopping_list.dto.ShoppingListItemRequest;
import com.whatwillieat.shopping_list.dto.ShoppingListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${app.API_V1_BASE_URL}/shopping-lists")
public class ShoppingListController {
    private final ShoppingListService shoppingListService;
    private final ShoppingListItemService shoppingListItemService;


    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService, ShoppingListItemService shoppingListItemService) {
        this.shoppingListService = shoppingListService;
        this.shoppingListItemService = shoppingListItemService;
    }

    // this is a test endpoint to check connection - it outputs 6090 which could be read as GOgO is one has too much imagination
    @GetMapping("/home")
    public String homeEndpoint() {
        return "6090";
    }

    @GetMapping("/owners/{ownerId}")
    public ResponseEntity<List<ShoppingListResponse>> getShoppingListsByOwner(@PathVariable UUID ownerId) {
        return ResponseEntity
                .ok(shoppingListService
                .getShoppingListsByOwner(ownerId));
    }

    @GetMapping("/{shoppingListId}")
    public ResponseEntity<ShoppingListResponse> getShoppingLisById(@PathVariable UUID shoppingListId) {
        ShoppingListResponse shoppingListResponse = shoppingListService.getShoppingListById(shoppingListId);
        return ResponseEntity.ok(shoppingListResponse);
    }

    @DeleteMapping("/{shoppingListId}/items/{itemId}")
    public ResponseEntity<Void> softDeleteItem(@PathVariable UUID shoppingListId, @PathVariable UUID itemId) {
        shoppingListItemService.softDelete(shoppingListId, itemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{shoppingListId}/items/{itemId}/undo-delete")
    public ResponseEntity<ShoppingListItemResponse> undoDeleteShoppingListItem(@PathVariable UUID shoppingListId, @PathVariable UUID itemId) {
        ShoppingListItemResponse response = shoppingListItemService.undoSoftDelete(shoppingListId, itemId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("{shoppingListId}/items")
    public ResponseEntity<ShoppingListItemResponse> createShoppingListItem(@PathVariable UUID shoppingListId, @RequestBody ShoppingListItemRequest shoppingListItemRequest) {
        shoppingListItemRequest.setShoppingListId(shoppingListId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(shoppingListItemService.save(shoppingListItemRequest));
    }

    @PostMapping
    public ResponseEntity<ShoppingListResponse> createShoppingList(@RequestBody ShoppingListRequest shoppingListRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(shoppingListService.create(shoppingListRequest));
    }

    @PutMapping("/{shoppingListId}")
    public ResponseEntity<ShoppingListResponse> updateShoppingList(@PathVariable UUID shoppingListId, @RequestBody ShoppingListRequest shoppingListRequest) {
        shoppingListRequest.setId(shoppingListId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(shoppingListService.update(shoppingListRequest));
    }

    @PutMapping("/{shoppingListId}/undo-delete")
    public ResponseEntity<ShoppingListResponse> undoDeleteShoppingList(@PathVariable UUID shoppingListId) {
        ShoppingListResponse response = shoppingListService.undoSoftDelete(shoppingListId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{shoppingListId}")
    public ResponseEntity<Void> deleteShoppingList(@PathVariable UUID shoppingListId) {
        shoppingListService.softDelete(shoppingListId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/owners/{ownerId}")
    public ResponseEntity<Void> deleteShoppingListByOwner(@PathVariable UUID ownerId) {
        shoppingListService.softDeleteShoppingListsByOwnerId(ownerId);
        return ResponseEntity.noContent().build();
    }


}
