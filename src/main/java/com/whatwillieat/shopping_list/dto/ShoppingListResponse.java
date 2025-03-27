package com.whatwillieat.shopping_list.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ShoppingListResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID ownerId;
    private boolean isDeleted;
    private List<ShoppingListItemResponse> items;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
