package com.hotel.reservation.service.impl;

import com.hotel.reservation.config.PricingProperties;
import com.hotel.reservation.domain.model.RoomType;
import com.hotel.reservation.domain.model.Season;
import com.hotel.reservation.service.PricingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class PricingServiceImpl implements PricingService {

    private final PricingProperties pricingProperties;

    public PricingServiceImpl(PricingProperties pricingProperties) {
        this.pricingProperties = pricingProperties;
    }

    @Override
    public Season getSeason(LocalDate date) {
        return pricingProperties.highSeasonMonths().contains(date.getMonthValue()) ? Season.HIGH : Season.LOW;
    }

    @Override
    public BigDecimal calculateRoomCost(RoomType type, BigDecimal basePricePerNight, LocalDate checkIn, LocalDate checkOut) {
        BigDecimal total = BigDecimal.ZERO;
        for (LocalDate night = checkIn; night.isBefore(checkOut); night = night.plusDays(1)) {
            if (getSeason(night) == Season.HIGH) {
                total = total.add(basePricePerNight.multiply(pricingProperties.highSeasonMultiplier()));
            } else {
                total = total.add(basePricePerNight);
            }
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
