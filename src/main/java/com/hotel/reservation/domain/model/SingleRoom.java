package com.hotel.reservation.domain.model;

import java.math.BigDecimal;

public class SingleRoom extends Room {

    public SingleRoom(String roomNumber, int floor, BigDecimal basePricePerNight) {
        super(roomNumber, floor, basePricePerNight);
    }

    @Override
    public RoomType getType() {
        return RoomType.SINGLE;
    }

    @Override
    public int getCapacity() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "Single room with one bed";
    }
}
