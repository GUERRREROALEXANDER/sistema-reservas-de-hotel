package com.hotel.reservation.domain.model;

import java.math.BigDecimal;

public abstract class Room {

    private final String roomNumber;
    private final int floor;
    private final BigDecimal basePricePerNight;
    private RoomStatus status;

    protected Room(String roomNumber, int floor, BigDecimal basePricePerNight) {
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.basePricePerNight = basePricePerNight;
        this.status = RoomStatus.AVAILABLE;
    }

    public abstract RoomType getType();

    public abstract int getCapacity();

    public abstract String getDescription();

    public void occupy() {
        this.status = RoomStatus.OCCUPIED;
    }

    public void release() {
        this.status = RoomStatus.AVAILABLE;
    }

    public boolean isOccupied() {
        return status == RoomStatus.OCCUPIED;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getFloor() {
        return floor;
    }

    public BigDecimal getBasePricePerNight() {
        return basePricePerNight;
    }

    public RoomStatus getStatus() {
        return status;
    }
}
