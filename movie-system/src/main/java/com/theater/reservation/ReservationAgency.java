package com.theater.reservation;

import com.theater.customer.Customer;
import com.theater.money.Money;
import com.theater.screening.Screening;

public class ReservationAgency {

    public Reservation reserve(Screening screening, Customer customer, int audienceCount) {
        Money fee = screening.calculateFee(audienceCount);
        return new Reservation(customer, screening, fee, audienceCount);
    }
}
