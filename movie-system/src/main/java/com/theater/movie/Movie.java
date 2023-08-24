package com.theater.movie;

import java.util.List;

public class Movie {

    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountConditino> discountConditinos;

    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;
}
