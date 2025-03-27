package com.whatwillieat.shopping_list;

import com.whatwillieat.shopping_list.model.ShoppingList;
import com.whatwillieat.shopping_list.repository.ShoppingListRepository;
import com.whatwillieat.shopping_list.service.ShoppingListService;
import com.whatwillieat.shopping_list.dto.ShoppingListRequest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTest {

    @Mock
    private ShoppingListRepository shoppingListRepository;

    @InjectMocks
    private ShoppingListService shoppingListService;

    private UUID shoppingListId;
    private UUID ownerId;
    private ShoppingList shoppingList;

    @BeforeEach
    void setUp() {
        shoppingListId = UUID.randomUUID();
        ownerId = UUID.randomUUID();

        shoppingList = new ShoppingList();
        shoppingList.setId(shoppingListId);
        shoppingList.setOwnerId(ownerId);
        shoppingList.setDeleted(false);
    }





    @Test
    void testUpdateShoppingList_NotFound() {
        ShoppingListRequest request = new ShoppingListRequest();
        request.setId(shoppingListId);

        when(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shoppingListService.update(request));
    }



    @Test
    void testGetShoppingListById_NotFound() {
        when(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shoppingListService.getShoppingListById(shoppingListId));
    }



    @Test
    void testSoftDeleteShoppingList() {
        when(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.of(shoppingList));

        shoppingListService.softDelete(shoppingListId);

        assertTrue(shoppingList.isDeleted());
        verify(shoppingListRepository, times(1)).save(shoppingList);
    }

    @Test
    void testSoftDeleteShoppingListsByOwnerId() {
        when(shoppingListRepository.findByOwnerIdAndIsDeletedFalse(ownerId)).thenReturn(List.of(shoppingList));

        shoppingListService.softDeleteShoppingListsByOwnerId(ownerId);

        assertTrue(shoppingList.isDeleted());
        verify(shoppingListRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testDeleteShoppingListById() {
        when(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.of(shoppingList));

        shoppingListService.delete(shoppingListId);

        verify(shoppingListRepository, times(1)).delete(shoppingList);
    }

    @Test
    void testDeleteByOwnerId() {
        shoppingListService.deleteByOwnerId(ownerId);

        verify(shoppingListRepository, times(1)).deleteByOwnerId(ownerId);
    }
}
