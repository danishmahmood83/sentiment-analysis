package com.socialsentiment.controller;

import com.socialsentiment.entity.TrackedSymbol;
import com.socialsentiment.repository.TrackedSymbolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackedSymbolControllerTest {

    @Mock
    private TrackedSymbolRepository repo;

    @InjectMocks
    private TrackedSymbolController controller;

    @Test
    void testGetAll() {
        when(repo.findAll()).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), controller.getAll());
    }

    @Test
    void testAddSymbol() {
        TrackedSymbol input = new TrackedSymbol();
        when(repo.save(input)).thenReturn(input);
        assertEquals(input, controller.addSymbol(input));
    }
}
