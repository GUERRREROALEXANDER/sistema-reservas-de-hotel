package com.hotel.reservation.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Invoice {

    private final String invoiceNumber;
    private final String reservationId;
    private final String guestName;
    private final String roomNumber;
    private final RoomType roomType;
    private final int nights;
    private final Season season;
    private final BigDecimal roomCost;
    private final List<InvoiceLine> serviceLines;
    private final BigDecimal servicesTotal;
    private final BigDecimal grandTotal;
    private final LocalDateTime issuedAt;

    public Invoice(String invoiceNumber, String reservationId, String guestName, String roomNumber,
                   RoomType roomType, int nights, Season season, BigDecimal roomCost,
                   List<InvoiceLine> serviceLines, BigDecimal servicesTotal, BigDecimal grandTotal,
                   LocalDateTime issuedAt) {
        this.invoiceNumber = invoiceNumber;
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.nights = nights;
        this.season = season;
        this.roomCost = roomCost;
        this.serviceLines = List.copyOf(serviceLines);
        this.servicesTotal = servicesTotal;
        this.grandTotal = grandTotal;
        this.issuedAt = issuedAt;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    public Season getSeason() {
        return season;
    }

    public BigDecimal getRoomCost() {
        return roomCost;
    }

    public List<InvoiceLine> getServiceLines() {
        return serviceLines;
    }

    public BigDecimal getServicesTotal() {
        return servicesTotal;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }
}
