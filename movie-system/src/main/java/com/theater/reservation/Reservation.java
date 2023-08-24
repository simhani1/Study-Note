package com.theater.reservation;

import com.theater.customer.Customer;
import com.theater.money.Money;
import com.theater.screening.Screening;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reservation {

    private Customer customer;
    private Screening screening;
    private Money fee;
    private int audienceCount;

    public Reservation(Customer customer, Screening screening, Money fee, int audienceCount) {
        this.customer = customer;
        this.screening = screening;
        this.fee = fee;
        this.audienceCount = audienceCount;
    }
}
