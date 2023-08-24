package com.theater.reservation;

import com.theater.customer.Customer;
import com.theater.money.Money;
import com.theater.screening.Screening;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Reservation {

    private Customer customer;
    private Screening screening;
    private Money fee;
    private int audienceCount;
}
