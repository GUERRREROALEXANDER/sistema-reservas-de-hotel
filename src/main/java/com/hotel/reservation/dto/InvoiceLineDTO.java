package com.hotel.reservation.dto;

import com.hotel.reservation.domain.model.InvoiceLine;

import java.math.BigDecimal;

public record InvoiceLineDTO(String description, int quantity, BigDecimal unitPrice, BigDecimal total) {

    public static InvoiceLineDTO from(InvoiceLine line) {
        return new InvoiceLineDTO(
                line.getDescription(),
                line.getQuantity(),
                line.getUnitPrice(),
                line.getTotal());
    }
}
