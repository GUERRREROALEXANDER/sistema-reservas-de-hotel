package com.hotel.reservation.domain.model;

import com.hotel.reservation.exception.InvalidRequestException;

public enum RoomType {
    SINGLE,
    DOUBLE,
    SUITE;

    public static RoomType fromString(String value) {
        if (value == null) {
            throw new InvalidRequestException("Room type cannot be null");
        }
        try {
            return RoomType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Unknown room type: " + value);
        }
    }
}
