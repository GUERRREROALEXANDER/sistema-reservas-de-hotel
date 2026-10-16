package com.hotel.reservation.service;

import com.hotel.reservation.domain.model.Invoice;
import com.hotel.reservation.domain.model.Reservation;

/**
 * Subsystem that produces the final invoice of a stay.
 */
public interface BillingService {

    Invoice generateInvoice(Reservation reservation);
}
