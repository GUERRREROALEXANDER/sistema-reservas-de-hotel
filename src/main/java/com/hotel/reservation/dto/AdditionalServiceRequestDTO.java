package com.hotel.reservation.dto;

import jakarta.validation.constraints.NotBlank;

public record AdditionalServiceRequestDTO(@NotBlank String serviceType) {
}
