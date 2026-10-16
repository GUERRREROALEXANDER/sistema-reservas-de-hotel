package com.hotel.reservation.domain.model;

import java.math.BigDecimal;

public class BreakfastService extends AdditionalService {

    private static final BigDecimal PRICE_PER_NIGHT = new BigDecimal("15.00");

    public BreakfastService() {
        super(ServiceType.BREAKFAST, PRICE_PER_NIGHT);
    }

    @Override
    public BigDecimal calculateCost(int nights) {
        return PRICE_PER_NIGHT.multiply(BigDecimal.valueOf(nights));
    }

    @Override
    public String getDescription() {
        return "Daily breakfast included";
    }
}
