package com.hotel.reservation.dto;

import com.hotel.reservation.domain.model.Reservation;

public record CheckInResponseDTO(
        String reservationId,
        String roomNumber,
        String digitalKey,
        String status,
        String message) {

    public static CheckInResponseDTO from(Reservation reservation, String digitalKey, String message) {
        return new CheckInResponseDTO(
                reservation.getId(),
                reservation.getRoom().getRoomNumber(),
                digitalKey,
                reservation.getStatus().name(),
                message);
    }
}
