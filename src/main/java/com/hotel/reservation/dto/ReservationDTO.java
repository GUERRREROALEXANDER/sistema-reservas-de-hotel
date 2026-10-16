package com.hotel.reservation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationDTO(
        @NotBlank String guestName,
        @NotBlank @Email String guestEmail,
        @NotBlank String roomType,
        @NotNull @FutureOrPresent LocalDate checkInDate,
        @NotNull @Future LocalDate checkOutDate) {
}
