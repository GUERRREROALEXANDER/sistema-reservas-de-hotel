package com.hotel.reservation.domain.model;

import java.math.BigDecimal;

public class SpaService extends AdditionalService {

    private static final BigDecimal FLAT_PRICE = new BigDecimal("50.00");

    public SpaService() {
        super(ServiceType.SPA, FLAT_PRICE);
    }

    @Override
    public BigDecimal calculateCost(int nights) {
        return FLAT_PRICE;
    }

    @Override
    public String getDescription() {
        return "Spa access package";
    }
}
