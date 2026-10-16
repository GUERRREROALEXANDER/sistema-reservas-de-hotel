package com.hotel.reservation.service.impl;

import com.hotel.reservation.domain.model.Reservation;
import com.hotel.reservation.domain.model.Room;
import com.hotel.reservation.domain.model.RoomType;
import com.hotel.reservation.exception.InvalidRequestException;
import com.hotel.reservation.exception.ResourceNotFoundException;
import com.hotel.reservation.exception.RoomNotAvailableException;
import com.hotel.reservation.repository.ReservationRepository;
import com.hotel.reservation.repository.RoomRepository;
import com.hotel.reservation.service.RoomService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public RoomServiceImpl(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut, RoomType type) {
        validateDateRange(checkIn, checkOut);
        List<Room> rooms = (type == null) ? roomRepository.findAll() : roomRepository.findByType(type);
        return rooms.stream()
                .filter(room -> isRoomAvailable(room, checkIn, checkOut))
                .sorted(Comparator.comparing(Room::getRoomNumber))
                .toList();
    }

    @Override
    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        return reservationRepository.findActiveByRoomNumber(room.getRoomNumber()).stream()
                .noneMatch(reservation -> reservation.overlaps(checkIn, checkOut));
    }

    @Override
    public synchronized Reservation createReservation(String guestName, String guestEmail, RoomType type,
                                                       LocalDate checkIn, LocalDate checkOut, BigDecimal roomCost) {
        List<Room> available = findAvailableRooms(checkIn, checkOut, type);
        if (available.isEmpty()) {
            throw new RoomNotAvailableException("No available room of type " + type + " for the requested dates");
        }
        Room room = available.get(0);
        Reservation reservation = new Reservation(guestName, guestEmail, room, checkIn, checkOut, roomCost);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation findReservation(String reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found: " + reservationId));
    }

    @Override
    public void occupyRoom(Reservation reservation) {
        reservation.getRoom().occupy();
        roomRepository.save(reservation.getRoom());
    }

    @Override
    public void releaseRoom(Reservation reservation) {
        reservation.getRoom().release();
        roomRepository.save(reservation.getRoom());
    }

    @Override
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    private void validateDateRange(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new InvalidRequestException("Check-out date must be after check-in date");
        }
    }
}
