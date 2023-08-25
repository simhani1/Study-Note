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

//    public Money calculateFee(int audienceCount) {
//        switch (movie.getMovieType()) {
//            case AMOUNT_DISCOUNT:
//                if (movie.isDiscountable(whenScreened, sequence)) {
//                    return movie.calculateAmountDiscountFee().times(audienceCount);
//                }
//                break;
//            case PERCENT_DISCOUNT:
//                if (movie.isDiscountable(whenScreened, sequence)) {
//                    return movie.calculatePercentDiscountFee().times(audienceCount);
//                }
//                break;
//            case NONE_DISCOUNT:
//                return movie.calculateNoneDiscountFee().times(audienceCount);
//        }
//        return movie.calculateNoneDiscountFee().times(audienceCount);
//    }

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
