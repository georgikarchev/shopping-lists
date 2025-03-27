package com.whatwillieat.shopping_list.dto.mapper;

import com.whatwillieat.shopping_list.dto.ShoppingListItemRequest;
import com.whatwillieat.shopping_list.dto.ShoppingListItemResponse;
import com.whatwillieat.shopping_list.dto.ShoppingListResponse;
import com.whatwillieat.shopping_list.model.ShoppingList;
import com.whatwillieat.shopping_list.dto.ShoppingListRequest;
import com.whatwillieat.shopping_list.model.ShoppingListItem;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {
    public static ShoppingList fromShoppingListRequest(ShoppingListRequest shoppingListRequest, ShoppingList existingShoppingList) {
        return ShoppingList.builder()
                .id(existingShoppingList != null ? existingShoppingList.getId() : null)
                .name(shoppingListRequest.getName())
                .description(shoppingListRequest.getDescription())
                .ownerId(existingShoppingList != null
                        ? existingShoppingList.getOwnerId()
                        : shoppingListRequest.getOwnerId() != null
                        ? shoppingListRequest.getOwnerId()
                        : null)
                .isDeleted(existingShoppingList != null ? existingShoppingList.isDeleted() : false)
                .createdOn(existingShoppingList != null ? existingShoppingList.getCreatedOn() : null)
                .build();
    }

    public static ShoppingListItemResponse toShoppingListItemResponse(ShoppingListItem shoppingListItem) {
        ShoppingListItemResponse response = ShoppingListItemResponse.builder()
                .id(shoppingListItem.getId())
                .name(shoppingListItem.getName())
                .description(shoppingListItem.getDescription())
                .isChecked(shoppingListItem.isChecked())
                .isDeleted(shoppingListItem.isDeleted())
                .build();
        return response;
    }
}
