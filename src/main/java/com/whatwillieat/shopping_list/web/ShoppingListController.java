package com.whatwillieat.shopping_list.web;

import com.whatwillieat.shopping_list.model.ShoppingList;
import com.whatwillieat.shopping_list.service.ShoppingListItemService;
import com.whatwillieat.shopping_list.service.ShoppingListService;
import com.whatwillieat.shopping_list.web.dto.ShoppingListRequest;
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

    @GetMapping("/home")
    public String homeEndpoint() {
        return "6090";
    }

    @GetMapping("/owners/{ownerId}")
    public ResponseEntity<List<ShoppingList>> getShoppingListsByOwner(@PathVariable UUID ownerId) {
        return ResponseEntity
                .ok(shoppingListService
                .getShoppingListsByOwner(ownerId));
    }

    @GetMapping("/{shoppingListId}")
    public ResponseEntity<ShoppingList> getShoppingLisById(@PathVariable UUID shoppingListId) {
        ShoppingList shoppingList = shoppingListService.getShoppingListById(shoppingListId);
        return ResponseEntity.ok(shoppingList);
    }

    @DeleteMapping("/{listId}/items/{itemId}")
    public ResponseEntity<Void> softDeleteItem(@PathVariable UUID listId, @PathVariable UUID itemId) {
        shoppingListItemService.softDelete(listId, itemId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ShoppingList> createShoppingList(@RequestBody ShoppingListRequest shoppingListRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(shoppingListService.create(shoppingListRequest));
    }

    @PutMapping("/{shoppingListId}")
    public ResponseEntity<ShoppingList> updateShoppingList(@PathVariable UUID shoppingListId, @RequestBody ShoppingListRequest shoppingListRequest) {
        shoppingListRequest.setId(shoppingListId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(shoppingListService.update(shoppingListRequest));
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
