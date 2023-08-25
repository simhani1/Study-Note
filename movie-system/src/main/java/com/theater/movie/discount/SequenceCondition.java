package com.theater.movie.discount;

import com.theater.screening.Screening;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SequenceCondition implements DiscountCondition {

    private int sequence;

    @Override
    public boolean isSatisfiedBy(Screening screening) {
        return sequence == screening.getSequence();
    }
}
