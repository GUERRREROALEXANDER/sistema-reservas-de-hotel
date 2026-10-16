package com.hotel.reservation.repository;

import com.hotel.reservation.domain.model.Room;
import com.hotel.reservation.domain.model.RoomType;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Room save(Room room);

    Optional<Room> findByNumber(String roomNumber);

    List<Room> findAll();

    List<Room> findByType(RoomType type);
}
