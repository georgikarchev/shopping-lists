package com.whatwillieat.shopping_list.dto;

import com.whatwillieat.shopping_list.model.ShoppingList;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ShoppingListItemResponse {
    private UUID id;
    private String name;
    private String description;
    private boolean isChecked;
    private boolean isDeleted;
}
