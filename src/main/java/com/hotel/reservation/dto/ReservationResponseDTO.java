package com.hotel.reservation.dto;

import com.hotel.reservation.domain.model.AdditionalService;
import com.hotel.reservation.domain.model.Reservation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public record ReservationResponseDTO(
        String reservationId,
        String guestName,
        String guestEmail,
        RoomDTO room,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        int nights,
        String status,
        BigDecimal roomCost,
        List<AdditionalServiceDTO> additionalServices,
        BigDecimal estimatedTotal,
        String digitalKey) {

    public static ReservationResponseDTO from(Reservation reservation) {
        List<AdditionalService> services = reservation.getAdditionalServices();
        int nights = reservation.getNights();
        BigDecimal servicesTotal = services.stream()
                .map(service -> service.calculateCost(nights))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal estimatedTotal = reservation.getRoomCost().add(servicesTotal)
                .setScale(2, RoundingMode.HALF_UP);
        List<AdditionalServiceDTO> serviceDTOs = services.stream()
                .map(service -> AdditionalServiceDTO.from(service, nights))
                .toList();
        return new ReservationResponseDTO(
                reservation.getId(),
                reservation.getGuestName(),
                reservation.getGuestEmail(),
                RoomDTO.from(reservation.getRoom()),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                nights,
                reservation.getStatus().name(),
                reservation.getRoomCost(),
                serviceDTOs,
                estimatedTotal,
                reservation.getDigitalKey());
    }
}
