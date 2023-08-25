package com.theater.movie;

import com.theater.screening.Screening;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class DiscountCondition {

    private DiscountConditionType type;

    private int sequence;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

//    public boolean isDiscountable(DayOfWeek dayOfWeek, LocalTime time) {
//        if (type != DiscountConditionType.PERIOD) {
//            throw new IllegalArgumentException();
//        }
//
//        return this.dayOfWeek.equals(dayOfWeek) &&
//                this.startTime.compareTo(time) <= 0 &&
//                this.endTime.compareTo(time) >= 0;
//    }
//
//    public boolean isDiscountable(int sequence) {
//        if (type != DiscountConditionType.SEQUENCE) {
//            throw new IllegalArgumentException();
//        }
//
//        return this.sequence == sequence;
//    }

    public boolean isSatisfiedBy(Screening screening) {
        if (type == DiscountConditionType.PERIOD) {
            return isSatisfiedByPeriod(screening);
        }
        return isSatisfiedBySequence(screening);
    }

    private boolean isSatisfiedByPeriod(Screening screening) {
        return dayOfWeek.equals(screening.getWhenScreened().getDayOfWeek()) &&
                startTime.compareTo(screening.getWhenScreened().toLocalTime()) <= 0 &&
                endTime.compareTo(screening.getWhenScreened().toLocalTime()) >= 0;
    }

    private boolean isSatisfiedBySequence(Screening screening) {
        return sequence == screening.getSequence();
    }
}
