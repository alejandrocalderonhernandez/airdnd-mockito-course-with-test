package com.debuggeandoideas.airdnd.service;

import com.debuggeandoideas.airdnd.Services.BookingService;
import com.debuggeandoideas.airdnd.Services.PaymentService;
import com.debuggeandoideas.airdnd.Services.RoomService;
import com.debuggeandoideas.airdnd.dto.BookingDto;
import com.debuggeandoideas.airdnd.dto.RoomDto;
import com.debuggeandoideas.airdnd.helpers.MailHelper;
import com.debuggeandoideas.airdnd.repositories.BookingRepository;
import com.debuggeandoideas.airdnd.utils.CurrencyConverter;
import com.debuggeandoideas.airdnd.utils.DataDummy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private PaymentService paymentServiceMock;
    @Mock
    private RoomService roomServiceMock;
    @Spy
    private BookingRepository bookingRepositoryMock;
    @Mock
    private MailHelper mailHelperMock;
    @Captor
    private  ArgumentCaptor<String> stringCapture;

    @InjectMocks
    private BookingService bookingService;

    @Test
    @DisplayName("getAvailablePlaceCount should works")
    void getAvailablePlaceCount() {
        when(this.roomServiceMock.findAllAvailableRooms())
                .thenReturn(DataDummy.default_rooms_list)
                .thenReturn(Collections.emptyList())
                .thenReturn(DataDummy.silgle_rooms_list);

        var expected1 = 14;
        var expected2 = 0;
        var expected3 = 5;

        var result1 = this.bookingService.getAvailablePlaceCount();
        var result2 = this.bookingService.getAvailablePlaceCount();
        var result3 = this.bookingService.getAvailablePlaceCount();

        assertAll(
                () -> assertEquals(expected1, result1 ),
                () -> assertEquals(expected2, result2 ),
                () -> assertEquals(expected3, result3 )
        );
    }

    @Test
    @DisplayName("booking happy path should works")
    void bookingHappyPath() {
        final var roomId = UUID.randomUUID().toString();

        doReturn(DataDummy.default_rooms_list.get(0))
                .when(this.roomServiceMock).findAvailableRoom(DataDummy.default_booking_req_3);

       doReturn(roomId)
              .when(this.bookingRepositoryMock).save(DataDummy.default_booking_req_3);

       doNothing()
              .when(this.roomServiceMock).bookRoom(anyString());

       doNothing()
               .when(this.mailHelperMock).sendMail(anyString(), anyString());

        var result = this.bookingService.booking(DataDummy.default_booking_req_3);

        assertEquals(roomId, result);
        verify(this.roomServiceMock, times(1)).findAvailableRoom(any(BookingDto.class));
        verify(this.bookingRepositoryMock, times(1)).save(any(BookingDto.class));
        verify(this.mailHelperMock, times(1)).sendMail(anyString(), anyString());
        verify(this.roomServiceMock, times(1)).bookRoom(anyString());
    }


    @Test
    @DisplayName("booking unhappy path should works")
    void bookingUnhappyPath() {
        doReturn(DataDummy.default_rooms_list.get(0))
                .when(this.roomServiceMock).findAvailableRoom(DataDummy.default_booking_req_2);

      doThrow(new IllegalArgumentException("Max 3 guest"))
              .when(this.paymentServiceMock).pay(eq(DataDummy.default_booking_req_2), eq(320.0));

        Executable executable = () -> this.bookingService.booking(DataDummy.default_booking_req_2);
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    @DisplayName("unbook should works")
    void unbook() {
        //given
        var id1 = "id1";
        var id2 = "id2";


        var bookingRes1 = DataDummy.default_booking_req_1;
        bookingRes1.setRoom(DataDummy.default_rooms_list.get(3));

        var bookingRes2 = DataDummy.default_booking_req_2;
        bookingRes2.setRoom(DataDummy.default_rooms_list.get(4));

        //when
        when(this.bookingRepositoryMock.findById(anyString()))
                .thenReturn(bookingRes1)
                .thenReturn(bookingRes2);

        doNothing()
                .when(this.roomServiceMock).unbookRoom(anyString());

        doNothing()
                .when(this.bookingRepositoryMock).deleteById(anyString());

        this.bookingService.unbook(id1);
        this.bookingService.unbook(id2);

        //then
        verify(this.roomServiceMock, times(2)).unbookRoom(anyString());
        verify(this.bookingRepositoryMock, times(2)).deleteById(anyString());
        verify(this.bookingRepositoryMock, times(2)).findById(this.stringCapture.capture());

        System.out.println("captured argument: " + this.stringCapture.getAllValues());

        assertEquals(List.of( "id1",  "id2"), this.stringCapture.getAllValues());
    }

    @Test
    @DisplayName("currencyConverter should works")
    void currencyConverter() {
        try (MockedStatic<CurrencyConverter> mockStatic = mockStatic(CurrencyConverter.class)) {

            final var expected = 900.0;

            mockStatic.when(() -> CurrencyConverter.toMx(anyDouble()))
                    .thenReturn(expected);

            var response = this.bookingService.calculateInMxn(DataDummy.default_booking_req_1);

            assertEquals(expected, response);
        }
    }

    @Test
    void shouldCountAvailablePlaces() {
        //Given
        given(this.roomServiceMock.findAllAvailableRooms())
                .willReturn(Collections.singletonList(new RoomDto("A1", 2)));

        var expected = 2;
        //When
        int response = this.bookingService.getAvailablePlaceCount();

        //Then
        then(roomServiceMock).should(times(1)).findAllAvailableRooms();
        assertEquals(expected, response);
    }

}
