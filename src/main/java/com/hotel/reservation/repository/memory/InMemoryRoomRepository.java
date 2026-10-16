package com.hotel.reservation.repository.memory;

import com.hotel.reservation.domain.model.Room;
import com.hotel.reservation.domain.model.RoomType;
import com.hotel.reservation.repository.RoomRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRoomRepository implements RoomRepository {

    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    @Override
    public Room save(Room room) {
        rooms.put(room.getRoomNumber(), room);
        return room;
    }

    @Override
    public Optional<Room> findByNumber(String roomNumber) {
        return Optional.ofNullable(rooms.get(roomNumber));
    }

    @Override
    public List<Room> findAll() {
        return rooms.values().stream()
                .sorted(Comparator.comparing(Room::getRoomNumber))
                .toList();
    }

    @Override
    public List<Room> findByType(RoomType type) {
        return rooms.values().stream()
                .filter(room -> room.getType() == type)
                .sorted(Comparator.comparing(Room::getRoomNumber))
                .toList();
    }
}
