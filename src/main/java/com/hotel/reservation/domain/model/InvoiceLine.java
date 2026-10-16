package com.hotel.reservation.domain.model;

import java.math.BigDecimal;

public class InvoiceLine {

    private final String description;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal total;

    public InvoiceLine(String description, int quantity, BigDecimal unitPrice, BigDecimal total) {
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
