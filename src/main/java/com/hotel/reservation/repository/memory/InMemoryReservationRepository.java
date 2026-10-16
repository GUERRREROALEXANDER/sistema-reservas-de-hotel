package com.hotel.reservation.repository.memory;

import com.hotel.reservation.domain.model.Reservation;
import com.hotel.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryReservationRepository implements ReservationRepository {

    private final ConcurrentHashMap<String, Reservation> reservations = new ConcurrentHashMap<>();

    @Override
    public Reservation save(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(String reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(reservations.values());
    }

    @Override
    public List<Reservation> findActiveByRoomNumber(String roomNumber) {
        return reservations.values().stream()
                .filter(reservation -> reservation.isActive()
                        && reservation.getRoom().getRoomNumber().equals(roomNumber))
                .toList();
    }
}
