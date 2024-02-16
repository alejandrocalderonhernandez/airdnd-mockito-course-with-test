package com.debuggeandoideas.airdnd.utils;

import com.debuggeandoideas.airdnd.dto.BookingDto;
import com.debuggeandoideas.airdnd.dto.RoomDto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataDummy {

    private DataDummy(){
    }

    public static final Map<RoomDto, Boolean> default_rooms = new HashMap<>() {{
        put(new RoomDto("A", 2), true);
        put(new RoomDto("B", 2), true);
        put(new RoomDto("C", 3), true);
        put(new RoomDto("D", 2), false);
        put(new RoomDto("E", 2), false);
        put(new RoomDto("F", 3), false);
    }};


    public static final List<RoomDto> default_rooms_list = List.of(
            new RoomDto("A", 2),
            new RoomDto("B", 2),
            new RoomDto("C", 3),
            new RoomDto("D", 2),
            new RoomDto("E", 2),
            new RoomDto("F", 3)
    );

    public static final List<RoomDto> silgle_rooms_list = List.of(
            new RoomDto("A", 5)
    );

    public static final BookingDto default_booking_req_1 = new BookingDto(
            "18318",
            LocalDate.of(2023, 06, 10),
            LocalDate.of(2023, 06, 20),
            2,
            false
    );

    public static final BookingDto default_booking_req_2 = new BookingDto(
            "3784193",
            LocalDate.of(2023, 06, 10),
            LocalDate.of(2023, 06, 26),
            2,
            true
    );

    public static final BookingDto default_booking_req_3 = new BookingDto(
            "48392392",
            LocalDate.of(2023, 05, 10),
            LocalDate.of(2023, 06, 26),
            2,
            false
    );


}
