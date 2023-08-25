package com.theater.reservation;

import com.theater.customer.Customer;
import com.theater.screening.Screening;

public class ReservationAgency {

    public Reservation reserve(Screening screening, Customer customer, int audienceCount) {
        return screening.reserve(customer, audienceCount);
    }
}
