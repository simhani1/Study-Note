package com.demo.architecture.order.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderedProduct {

    private String productName;
    private int quantity;
    private int price;

    @Builder
    public OrderedProduct(String productName, int quantity, int price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
}
