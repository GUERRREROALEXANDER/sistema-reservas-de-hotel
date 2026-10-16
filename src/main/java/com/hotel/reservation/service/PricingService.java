package com.hotel.reservation.service;

import com.hotel.reservation.domain.model.RoomType;
import com.hotel.reservation.domain.model.Season;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Subsystem that applies the seasonal pricing rules.
 */
public interface PricingService {

    Season getSeason(LocalDate date);

    /** Prices every night individually, so a stay crossing seasons is charged correctly. */
    BigDecimal calculateRoomCost(RoomType type, BigDecimal basePricePerNight, LocalDate checkIn, LocalDate checkOut);
}
