package com.theater.reservation;

import com.theater.customer.Customer;
import com.theater.money.Money;
import com.theater.movie.DiscountCondition;
import com.theater.movie.DiscountConditionType;
import com.theater.movie.Movie;
import com.theater.movie.MovieType;
import com.theater.screening.Screening;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationAgencyTest {

    private ReservationAgency reservationAgency = new ReservationAgency();

    @DisplayName("할인 조건에 따라 할인을 적용시킨 후 영화 예매 정보를 저장한다")
    @Test
    void reserveMovie() {
        // given
        Screening screening = Screening.builder()
                .movie(Movie.builder()
                        .title("영화")
                        .fee(Money.wons(10000))
                        .discountAmount(Money.wons(1000))
                        .discountPercent(0.0)
                        .discountConditions(List.of(DiscountCondition.builder()
                                .dayOfWeek(DayOfWeek.FRIDAY)
                                .sequence(1)
                                .startTime(LocalTime.of(1, 20))
                                .endTime(LocalTime.of(2, 30))
                                .type(DiscountConditionType.SEQUENCE)
                                .build()))
                        .movieType(MovieType.AMOUNT_DISCOUNT)
                        .build())
                .sequence(1)
                .whenScreened(LocalDateTime.of(2023, 1, 1, 13, 30, 0))
                .build();
        Customer customer = Customer.builder()
                .id("test")
                .name("이름")
                .build();

        int audienceCount = 2;

        // when
        Reservation reserveation = reservationAgency.reserve(screening, customer, audienceCount);

        // then
        assertThat(reserveation.getCustomer()).isEqualTo(customer);
        assertThat(reserveation.getScreening()).isEqualTo(screening);
        assertThat(reserveation.getAudienceCount()).isEqualTo(audienceCount);
        assertThat(reserveation.getFee().getAmount().intValue()).isEqualTo(2 * 9000);
    }
}