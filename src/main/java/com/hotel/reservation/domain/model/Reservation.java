package com.hotel.reservation.domain.model;

import com.hotel.reservation.exception.InvalidReservationStateException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Reservation {

    private final String id;
    private final String guestName;
    private final String guestEmail;
    private final Room room;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private ReservationStatus status;
    private final BigDecimal roomCost;
    private final List<AdditionalService> additionalServices;
    private String digitalKey;
    private Invoice invoice;
    private final LocalDateTime createdAt;

    public Reservation(String guestName, String guestEmail, Room room,
                       LocalDate checkInDate, LocalDate checkOutDate, BigDecimal roomCost) {
        this.id = UUID.randomUUID().toString();
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = ReservationStatus.CONFIRMED;
        this.roomCost = roomCost;
        this.additionalServices = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    public int getNights() {
        return (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public void addService(AdditionalService service) {
        additionalServices.add(service);
    }

    public void checkIn(String digitalKey) {
        if (status != ReservationStatus.CONFIRMED) {
            throw new InvalidReservationStateException("Reservation must be CONFIRMED to check in");
        }
        this.digitalKey = digitalKey;
        this.status = ReservationStatus.CHECKED_IN;
    }

    public void checkOut(Invoice invoice) {
        if (status != ReservationStatus.CHECKED_IN) {
            throw new InvalidReservationStateException("Reservation must be CHECKED_IN to check out");
        }
        this.invoice = invoice;
        this.digitalKey = null;
        this.status = ReservationStatus.CHECKED_OUT;
    }

    public boolean isActive() {
        return status == ReservationStatus.CONFIRMED || status == ReservationStatus.CHECKED_IN;
    }

    public boolean overlaps(LocalDate from, LocalDate to) {
        return from.isBefore(checkOutDate) && to.isAfter(checkInDate);
    }

    public String getId() {
        return id;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public BigDecimal getRoomCost() {
        return roomCost;
    }

    public List<AdditionalService> getAdditionalServices() {
        return Collections.unmodifiableList(additionalServices);
    }

    public String getDigitalKey() {
        return digitalKey;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
