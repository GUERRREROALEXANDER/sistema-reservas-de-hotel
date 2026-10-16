package com.hotel.reservation.domain.model;

import java.math.BigDecimal;

public abstract class AdditionalService {

    private final ServiceType type;
    private final BigDecimal unitPrice;

    protected AdditionalService(ServiceType type, BigDecimal unitPrice) {
        this.type = type;
        this.unitPrice = unitPrice;
    }

    public abstract BigDecimal calculateCost(int nights);

    public abstract String getDescription();

    public ServiceType getType() {
        return type;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}
