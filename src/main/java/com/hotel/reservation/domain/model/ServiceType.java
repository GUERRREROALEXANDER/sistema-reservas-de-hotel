package com.hotel.reservation.domain.model;

import com.hotel.reservation.exception.InvalidRequestException;

public enum ServiceType {
    SPA,
    BREAKFAST,
    AIRPORT_TRANSPORT;

    public static ServiceType fromString(String value) {
        if (value == null) {
            throw new InvalidRequestException("Service type cannot be null");
        }
        String normalized = value.trim().toUpperCase().replace(' ', '_').replace('-', '_');
        try {
            return ServiceType.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Unknown service type: " + value);
        }
    }
}
