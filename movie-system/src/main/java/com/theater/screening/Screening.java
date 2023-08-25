package com.theater.screening;

import com.theater.customer.Customer;
import com.theater.money.Money;
import com.theater.movie.Movie;
import com.theater.reservation.Reservation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Screening {

    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;

    public Reservation reserve(Customer customer, int audienceCount) {
        return Reservation.builder()
                .customer(customer)
                .screening(this)
                .fee(calculateFee(audienceCount))
                .audienceCount(audienceCount)
                .build();
    }

    private Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }
}
