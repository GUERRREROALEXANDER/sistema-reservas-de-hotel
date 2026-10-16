package com.hotel.reservation.controller;

import com.hotel.reservation.dto.AdditionalServiceRequestDTO;
import com.hotel.reservation.dto.CheckInResponseDTO;
import com.hotel.reservation.dto.InvoiceDTO;
import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.dto.ReservationResponseDTO;
import com.hotel.reservation.dto.RoomDTO;
import com.hotel.reservation.facade.HotelFacade;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * REST adapter of the hotel reservation system. Delegates every operation to the
 * {@link HotelFacade} and knows nothing about the underlying services.
 */
@RestController
@RequestMapping("/api/hotel")
@CrossOrigin(origins = "*")
public class HotelController {

    private final HotelFacade hotelFacade;

    public HotelController(HotelFacade hotelFacade) {
        this.hotelFacade = hotelFacade;
    }

    @PostMapping("/reservar")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDTO createReservation(@Valid @RequestBody ReservationDTO dto) {
        return hotelFacade.createReservation(dto);
    }

    @GetMapping("/disponibilidad")
    public List<RoomDTO> checkAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false) String roomType) {
        return hotelFacade.checkAvailability(checkIn, checkOut, roomType);
    }

    @PostMapping("/servicios/{reservationId}")
    public ReservationResponseDTO addService(@PathVariable String reservationId,
                                             @Valid @RequestBody AdditionalServiceRequestDTO request) {
        return hotelFacade.addService(reservationId, request.serviceType());
    }

    @PutMapping("/checkin/{reservationId}")
    public CheckInResponseDTO performCheckIn(@PathVariable String reservationId) {
        return hotelFacade.performCheckIn(reservationId);
    }

    @PutMapping("/checkout/{reservationId}")
    public InvoiceDTO performCheckOut(@PathVariable String reservationId) {
        return hotelFacade.performCheckOut(reservationId);
    }

    @GetMapping("/reserva/{reservationId}")
    public ReservationResponseDTO getReservation(@PathVariable String reservationId) {
        return hotelFacade.getReservation(reservationId);
    }
}
