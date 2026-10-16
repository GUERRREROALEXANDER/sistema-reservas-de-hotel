package com.hotel.reservation.domain.model;

import java.math.BigDecimal;

public class SuiteRoom extends Room {

    public SuiteRoom(String roomNumber, int floor, BigDecimal basePricePerNight) {
        super(roomNumber, floor, basePricePerNight);
    }

    @Override
    public RoomType getType() {
        return RoomType.SUITE;
    }

    @Override
    public int getCapacity() {
        return 4;
    }

    @Override
    public String getDescription() {
        return "Luxury suite with living area";
    }
}
