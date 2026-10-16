package com.hotel.reservation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.Set;

@ConfigurationProperties(prefix = "hotel.pricing")
public record PricingProperties(Set<Integer> highSeasonMonths, BigDecimal highSeasonMultiplier) {
}
