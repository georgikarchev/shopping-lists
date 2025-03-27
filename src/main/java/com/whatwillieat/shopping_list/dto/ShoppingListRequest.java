package com.whatwillieat.shopping_list.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Data
public class ShoppingListRequest {

    private UUID id;

    @NotNull
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private UUID ownerId;

    private boolean isDeleted;
}
