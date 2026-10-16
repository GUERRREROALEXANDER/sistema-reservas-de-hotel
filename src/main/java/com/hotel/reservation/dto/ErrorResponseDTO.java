package com.hotel.reservation.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(int status, String error, String message, LocalDateTime timestamp) {
}
