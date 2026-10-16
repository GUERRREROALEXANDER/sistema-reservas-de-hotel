package com.hotel.reservation.service;

import com.hotel.reservation.domain.model.AdditionalService;
import com.hotel.reservation.domain.model.Reservation;
import com.hotel.reservation.domain.model.ServiceType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Subsystem that builds and prices the extra services a guest can request.
 */
public interface AdditionalServiceService {

    AdditionalService createService(ServiceType type);

    BigDecimal calculateServicesTotal(Reservation reservation);

    List<AdditionalService> getCatalog();
}
