package com.whatwillieat.shopping_list.service.mapper;

import com.whatwillieat.shopping_list.model.ShoppingList;
import com.whatwillieat.shopping_list.web.dto.ShoppingListRequest;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class DtoMapper {
    public static ShoppingList fromShoppingListRequest(ShoppingListRequest shoppingListRequest, ShoppingList existingShoppingList) {
        return ShoppingList.builder()
                .id(existingShoppingList != null ? existingShoppingList.getId() : null)
                .name(shoppingListRequest.getName())
                .description(shoppingListRequest.getDescription())
                .ownerId(shoppingListRequest.getOwnerId())
                .isDeleted(existingShoppingList != null ? existingShoppingList.isDeleted() : false)
                .build();
    }
}
