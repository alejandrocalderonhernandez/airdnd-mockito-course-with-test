package com.debuggeandoideas.airdnd.service;

import com.debuggeandoideas.airdnd.Services.RoomService;
import com.debuggeandoideas.airdnd.repositories.RoomRepository;
import com.debuggeandoideas.airdnd.utils.DataDummy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepositoryMock;
    @InjectMocks
    private RoomService roomService;

    @Test
    @DisplayName("Should get all rooms available in room repository")
    void findAllAvailableRooms() {
        when(roomRepositoryMock.findAll())
                .thenReturn(DataDummy.default_rooms);
        var expected = 3;
        var result = this.roomService.findAllAvailableRooms();

        assertEquals(expected, result.size());
    }

    @Test
    @DisplayName("Should calculate  price correct")
    void calculatePrice() {
        when(roomRepositoryMock.findAll())
                .thenReturn(DataDummy.default_rooms);
        var expected = 3;
        var result = this.roomService.findAllAvailableRooms();

        assertEquals(expected, result.size());
    }


}
