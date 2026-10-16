package com.hotel.reservation.service.impl;

import com.hotel.reservation.domain.model.AdditionalService;
import com.hotel.reservation.domain.model.AirportTransportService;
import com.hotel.reservation.domain.model.BreakfastService;
import com.hotel.reservation.domain.model.Reservation;
import com.hotel.reservation.domain.model.ServiceType;
import com.hotel.reservation.domain.model.SpaService;
import com.hotel.reservation.service.AdditionalServiceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class AdditionalServiceServiceImpl implements AdditionalServiceService {

    @Override
    public AdditionalService createService(ServiceType type) {
        return switch (type) {
            case SPA -> new SpaService();
            case BREAKFAST -> new BreakfastService();
            case AIRPORT_TRANSPORT -> new AirportTransportService();
        };
    }

    @Override
    public BigDecimal calculateServicesTotal(Reservation reservation) {
        int nights = reservation.getNights();
        BigDecimal total = BigDecimal.ZERO;
        for (AdditionalService service : reservation.getAdditionalServices()) {
            total = total.add(service.calculateCost(nights));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public List<AdditionalService> getCatalog() {
        return List.of(new SpaService(), new BreakfastService(), new AirportTransportService());
    }
}
