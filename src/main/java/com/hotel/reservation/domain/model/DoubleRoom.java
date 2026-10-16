package com.hotel.reservation.domain.model;

import java.math.BigDecimal;

public class DoubleRoom extends Room {

    public DoubleRoom(String roomNumber, int floor, BigDecimal basePricePerNight) {
        super(roomNumber, floor, basePricePerNight);
    }

    @Override
    public RoomType getType() {
        return RoomType.DOUBLE;
    }

    @Override
    public int getCapacity() {
        return 2;
    }

    @Override
    public String getDescription() {
        return "Double room with one double bed";
    }
}
