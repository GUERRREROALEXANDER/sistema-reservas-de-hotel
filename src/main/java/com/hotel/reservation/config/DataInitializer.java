package com.hotel.reservation.config;

import com.hotel.reservation.domain.model.DoubleRoom;
import com.hotel.reservation.domain.model.Room;
import com.hotel.reservation.domain.model.SingleRoom;
import com.hotel.reservation.domain.model.SuiteRoom;
import com.hotel.reservation.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoomRepository roomRepository;

    public DataInitializer(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) {
        List<Room> rooms = buildRooms();
        rooms.forEach(roomRepository::save);
        log.info("Loaded {} rooms at startup", roomRepository.findAll().size());
    }

    private List<Room> buildRooms() {
        List<Room> rooms = new ArrayList<>();

        BigDecimal singlePrice = new BigDecimal("80.00");
        for (int i = 1; i <= 6; i++) {
            rooms.add(new SingleRoom("10" + i, 1, singlePrice));
        }

        BigDecimal doublePrice = new BigDecimal("120.00");
        for (int i = 1; i <= 5; i++) {
            rooms.add(new DoubleRoom("20" + i, 2, doublePrice));
        }

        BigDecimal suitePrice = new BigDecimal("250.00");
        for (int i = 1; i <= 4; i++) {
            rooms.add(new SuiteRoom("30" + i, 3, suitePrice));
        }

        return rooms;
    }
}
