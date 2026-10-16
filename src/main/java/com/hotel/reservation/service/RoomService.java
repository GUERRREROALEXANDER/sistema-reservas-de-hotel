package com.hotel.reservation.service;

import com.hotel.reservation.domain.model.Reservation;
import com.hotel.reservation.domain.model.Room;
import com.hotel.reservation.domain.model.RoomType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Subsystem in charge of room inventory, availability and reservation records.
 */
public interface RoomService {

    /** Rooms without an active reservation overlapping the range. A null type means every type. */
    List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut, RoomType type);

    boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut);

    Reservation createReservation(String guestName, String guestEmail, RoomType type,
                                  LocalDate checkIn, LocalDate checkOut, BigDecimal roomCost);

    Reservation findReservation(String reservationId);

    void occupyRoom(Reservation reservation);

    void releaseRoom(Reservation reservation);

    Reservation save(Reservation reservation);
}
