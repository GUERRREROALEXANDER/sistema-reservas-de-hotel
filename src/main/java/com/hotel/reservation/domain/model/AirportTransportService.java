package com.hotel.reservation.domain.model;

import java.math.BigDecimal;

public class AirportTransportService extends AdditionalService {

    private static final BigDecimal FLAT_PRICE = new BigDecimal("30.00");

    public AirportTransportService() {
        super(ServiceType.AIRPORT_TRANSPORT, FLAT_PRICE);
    }

    @Override
    public BigDecimal calculateCost(int nights) {
        return FLAT_PRICE;
    }

    @Override
    public String getDescription() {
        return "Airport transfer service";
    }
}
