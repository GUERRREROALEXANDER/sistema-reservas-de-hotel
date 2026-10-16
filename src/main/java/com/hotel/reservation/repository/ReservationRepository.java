package com.hotel.reservation.repository;

import com.hotel.reservation.domain.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(String reservationId);

    List<Reservation> findAll();

    List<Reservation> findActiveByRoomNumber(String roomNumber);
}
