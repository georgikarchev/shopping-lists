package com.whatwillieat.shopping_list.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ShoppingListItemRequest {

    private UUID id;

    @NotNull
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private UUID shoppingListId;

    private boolean isChecked;

    private boolean isDeleted;
}
