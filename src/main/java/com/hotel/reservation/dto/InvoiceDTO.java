package com.hotel.reservation.dto;

import com.hotel.reservation.domain.model.Invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record InvoiceDTO(
        String invoiceNumber,
        String reservationId,
        String guestName,
        String roomNumber,
        String roomType,
        int nights,
        String season,
        BigDecimal roomCost,
        List<InvoiceLineDTO> serviceLines,
        BigDecimal servicesTotal,
        BigDecimal grandTotal,
        LocalDateTime issuedAt) {

    public static InvoiceDTO from(Invoice invoice) {
        List<InvoiceLineDTO> lines = invoice.getServiceLines().stream()
                .map(InvoiceLineDTO::from)
                .toList();
        return new InvoiceDTO(
                invoice.getInvoiceNumber(),
                invoice.getReservationId(),
                invoice.getGuestName(),
                invoice.getRoomNumber(),
                invoice.getRoomType().name(),
                invoice.getNights(),
                invoice.getSeason().name(),
                invoice.getRoomCost(),
                lines,
                invoice.getServicesTotal(),
                invoice.getGrandTotal(),
                invoice.getIssuedAt());
    }
}
