package com.whatwillieat.shopping_list.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("items")
    private ShoppingList shoppingList;

    private boolean isChecked;

    private boolean isDeleted;

    @Override
    public String toString() {
        return "ShoppingListItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isChecked=" + isChecked +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
