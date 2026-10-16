package com.hotel.reservation.service.impl;

import com.hotel.reservation.domain.model.Reservation;
import com.hotel.reservation.service.AccessService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccessServiceImpl implements AccessService {

    private final Map<String, String> activeKeys = new ConcurrentHashMap<>();

    @Override
    public String generateDigitalKey(Reservation reservation) {
        String key = UUID.randomUUID().toString();
        activeKeys.put(reservation.getId(), key);
        return key;
    }

    @Override
    public boolean validateKey(String reservationId, String digitalKey) {
        String stored = activeKeys.get(reservationId);
        return stored != null && stored.equals(digitalKey);
    }

    @Override
    public void revokeKey(String reservationId) {
        activeKeys.remove(reservationId);
    }
}
