package com.hotel.reservation.service.impl;

import com.hotel.reservation.domain.model.AdditionalService;
import com.hotel.reservation.domain.model.Invoice;
import com.hotel.reservation.domain.model.InvoiceLine;
import com.hotel.reservation.domain.model.Reservation;
import com.hotel.reservation.domain.model.Season;
import com.hotel.reservation.domain.model.ServiceType;
import com.hotel.reservation.service.AdditionalServiceService;
import com.hotel.reservation.service.BillingService;
import com.hotel.reservation.service.PricingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BillingServiceImpl implements BillingService {

    private final PricingService pricingService;
    private final AdditionalServiceService additionalServiceService;

    public BillingServiceImpl(PricingService pricingService, AdditionalServiceService additionalServiceService) {
        this.pricingService = pricingService;
        this.additionalServiceService = additionalServiceService;
    }

    @Override
    public Invoice generateInvoice(Reservation reservation) {
        int nights = reservation.getNights();
        List<InvoiceLine> serviceLines = new ArrayList<>();

        for (AdditionalService service : reservation.getAdditionalServices()) {
            int quantity = (service.getType() == ServiceType.BREAKFAST) ? nights : 1;
            BigDecimal total = service.calculateCost(nights).setScale(2, RoundingMode.HALF_UP);
            serviceLines.add(new InvoiceLine(service.getDescription(), quantity, service.getUnitPrice(), total));
        }
        BigDecimal servicesTotal = additionalServiceService.calculateServicesTotal(reservation);

        BigDecimal grandTotal = reservation.getRoomCost().add(servicesTotal).setScale(2, RoundingMode.HALF_UP);
        Season season = pricingService.getSeason(reservation.getCheckInDate());
        String invoiceNumber = "INV-" + randomSuffix();

        return new Invoice(invoiceNumber, reservation.getId(), reservation.getGuestName(),
                reservation.getRoom().getRoomNumber(), reservation.getRoom().getType(), nights, season,
                reservation.getRoomCost().setScale(2, RoundingMode.HALF_UP), serviceLines,
                servicesTotal, grandTotal, LocalDateTime.now());
    }

    private String randomSuffix() {
        String raw = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return raw.substring(0, 8);
    }
}
