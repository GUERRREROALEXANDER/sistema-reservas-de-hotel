package com.hotel.reservation.dto;

import com.hotel.reservation.domain.model.AdditionalService;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record AdditionalServiceDTO(String type, String description, BigDecimal cost) {

    public static AdditionalServiceDTO from(AdditionalService service, int nights) {
        BigDecimal cost = service.calculateCost(nights).setScale(2, RoundingMode.HALF_UP);
        return new AdditionalServiceDTO(service.getType().name(), service.getDescription(), cost);
    }
}
