package com.hotel.reservation.dto;

import com.hotel.reservation.domain.model.Room;

import java.math.BigDecimal;

public record RoomDTO(
        String roomNumber,
        String type,
        int floor,
        int capacity,
        BigDecimal basePricePerNight,
        String description) {

    public static RoomDTO from(Room room) {
        return new RoomDTO(
                room.getRoomNumber(),
                room.getType().name(),
                room.getFloor(),
                room.getCapacity(),
                room.getBasePricePerNight(),
                room.getDescription());
    }
}
