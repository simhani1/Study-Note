package com.theater.movie.discount;

import com.theater.screening.Screening;

public interface DiscountCondition {

    boolean isSatisfiedBy(Screening screening);
}
