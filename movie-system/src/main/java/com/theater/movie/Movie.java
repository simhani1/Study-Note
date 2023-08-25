package com.theater.movie;

import com.theater.money.Money;
import com.theater.screening.Screening;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Movie {

    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountCondition> discountConditions;

    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;

//    public Money calculateAmountDiscountFee() {
//        if (movieType != MovieType.AMOUNT_DISCOUNT) {
//            throw new IllegalArgumentException();
//        }
//        return fee.minus(discountAmount);
//    }
//
//    public Money calculatePercentDiscountFee() {
//        if (movieType != MovieType.PERCENT_DISCOUNT) {
//            throw new IllegalArgumentException();
//        }
//        return fee.minus(fee.times(discountPercent));
//    }
//
//    public Money calculateNoneDiscountFee() {
//        if (movieType != MovieType.NONE_DISCOUNT) {
//            throw new IllegalArgumentException();
//        }
//        return fee;
//    }

//    public boolean isDiscountable(LocalDateTime whenScreened, int sequence) {
//        for (DiscountCondition condition : discountConditions) {
//            if (condition.getType() == DiscountConditionType.PERIOD) {
//                if (condition.isDiscountable(whenScreened.getDayOfWeek(), whenScreened.toLocalTime())) {
//                    return true;
//                }
//            } else {
//                if (condition.isDiscountable(sequence)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public Money calculateMovieFee(Screening screening) {
        if (isDiscountable(screening)) {
            return fee.minus(calculateDiscountAmount());
        }
        return fee;
    }

    private boolean isDiscountable(Screening screening) {
        return discountConditions.stream()
                .anyMatch(condition -> condition.isSatisfied(screening));
    }

    private Money calculateDiscountAmount() {
        switch (movieType) {
            case AMOUNT_DISCOUNT:
                return calculateAmountDiscountAmount();
            case PERCENT_DISCOUNT:
                return calculatePercentDiscountAmount();
            case NONE_DISCOUNT:
                return calculateNoneDiscountAmount();
        }
        throw new IllegalArgumentException();
    }

    private Money calculateAmountDiscountAmount() {
        return discountAmount;
    }

    private Money calculatePercentDiscountAmount() {
        return fee.times(discountPercent);
    }

    private Money calculateNoneDiscountAmount() {
        return Money.ZERO;
    }
}
