package com.hotel.reservation.service;

import com.hotel.reservation.domain.model.Reservation;

/**
 * Subsystem that issues and revokes the simulated digital room keys.
 */
public interface AccessService {

    String generateDigitalKey(Reservation reservation);

    boolean validateKey(String reservationId, String digitalKey);

    void revokeKey(String reservationId);
}
