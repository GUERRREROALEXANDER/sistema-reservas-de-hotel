package com.hotel.reservation.facade;

import com.hotel.reservation.domain.model.AdditionalService;
import com.hotel.reservation.domain.model.Invoice;
import com.hotel.reservation.domain.model.Reservation;
import com.hotel.reservation.domain.model.Room;
import com.hotel.reservation.domain.model.RoomType;
import com.hotel.reservation.domain.model.ServiceType;
import com.hotel.reservation.dto.CheckInResponseDTO;
import com.hotel.reservation.dto.InvoiceDTO;
import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.dto.ReservationResponseDTO;
import com.hotel.reservation.dto.RoomDTO;
import com.hotel.reservation.exception.InvalidReservationStateException;
import com.hotel.reservation.exception.RoomNotAvailableException;
import com.hotel.reservation.service.AccessService;
import com.hotel.reservation.service.AdditionalServiceService;
import com.hotel.reservation.service.BillingService;
import com.hotel.reservation.service.PricingService;
import com.hotel.reservation.service.RoomService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Facade of the hotel reservation system.
 *
 * <p>Single entry point that orchestrates the five subsystems - room inventory,
 * seasonal pricing, additional services, billing and access control - behind a
 * simple, coarse-grained API expressed only in DTOs. Clients (the web controller)
 * never touch the domain model or the individual services, which keeps them
 * decoupled from the internal wiring and from the order in which the subsystems
 * must collaborate.</p>
 */
@Component
public class HotelFacade {

    private final RoomService roomService;
    private final PricingService pricingService;
    private final AdditionalServiceService additionalServiceService;
    private final BillingService billingService;
    private final AccessService accessService;

    public HotelFacade(RoomService roomService,
                       PricingService pricingService,
                       AdditionalServiceService additionalServiceService,
                       BillingService billingService,
                       AccessService accessService) {
        this.roomService = roomService;
        this.pricingService = pricingService;
        this.additionalServiceService = additionalServiceService;
        this.billingService = billingService;
        this.accessService = accessService;
    }

    public ReservationResponseDTO createReservation(ReservationDTO dto) {
        RoomType type = RoomType.fromString(dto.roomType());
        List<Room> availableRooms =
                roomService.findAvailableRooms(dto.checkInDate(), dto.checkOutDate(), type);
        if (availableRooms.isEmpty()) {
            throw new RoomNotAvailableException(
                    "No " + type + " room available for the requested dates");
        }
        BigDecimal basePricePerNight = availableRooms.get(0).getBasePricePerNight();
        BigDecimal roomCost = pricingService.calculateRoomCost(
                type, basePricePerNight, dto.checkInDate(), dto.checkOutDate());
        Reservation reservation = roomService.createReservation(
                dto.guestName(), dto.guestEmail(), type,
                dto.checkInDate(), dto.checkOutDate(), roomCost);
        return ReservationResponseDTO.from(reservation);
    }

    public List<RoomDTO> checkAvailability(LocalDate checkIn, LocalDate checkOut, String roomType) {
        RoomType type = (roomType == null || roomType.isBlank()) ? null : RoomType.fromString(roomType);
        return roomService.findAvailableRooms(checkIn, checkOut, type).stream()
                .map(RoomDTO::from)
                .toList();
    }

    public ReservationResponseDTO addService(String reservationId, String serviceType) {
        Reservation reservation = roomService.findReservation(reservationId);
        if (!reservation.isActive()) {
            throw new InvalidReservationStateException(
                    "Additional services can only be added to active reservations");
        }
        ServiceType type = ServiceType.fromString(serviceType);
        AdditionalService service = additionalServiceService.createService(type);
        reservation.addService(service);
        roomService.save(reservation);
        return ReservationResponseDTO.from(reservation);
    }

    public CheckInResponseDTO performCheckIn(String reservationId) {
        Reservation reservation = roomService.findReservation(reservationId);
        String digitalKey = accessService.generateDigitalKey(reservation);
        reservation.checkIn(digitalKey);
        roomService.occupyRoom(reservation);
        roomService.save(reservation);
        return CheckInResponseDTO.from(reservation, digitalKey, "Check-in completed successfully");
    }

    public InvoiceDTO performCheckOut(String reservationId) {
        Reservation reservation = roomService.findReservation(reservationId);
        Invoice invoice = billingService.generateInvoice(reservation);
        reservation.checkOut(invoice);
        roomService.releaseRoom(reservation);
        accessService.revokeKey(reservationId);
        roomService.save(reservation);
        return InvoiceDTO.from(invoice);
    }

    public ReservationResponseDTO getReservation(String reservationId) {
        return ReservationResponseDTO.from(roomService.findReservation(reservationId));
    }
}
