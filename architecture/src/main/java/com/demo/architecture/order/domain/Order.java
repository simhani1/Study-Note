package com.demo.architecture.order.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Order {

    private Long ordererId;
    private List<OrderedProduct> orderedProducts = new ArrayList<>();
    private int totalPrice;
    private OrderStatus orderStatus;

    @Builder
    public Order(Long ordererId, List<OrderedProduct> orderedProducts, String orderStatus) {
        this.ordererId = ordererId;
        this.orderedProducts = orderedProducts;
        this.totalPrice = calcTotalPrice(orderedProducts);
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    @Builder
    public Order(List<OrderedProduct> orderedProducts) {
        this.orderedProducts = orderedProducts;
        this.totalPrice = calcTotalPrice(orderedProducts);
        this.orderStatus = OrderStatus.PREPARING;
    }

    private int calcTotalPrice(List<OrderedProduct> orderedProducts) {
        return orderedProducts.stream()
                .mapToInt(p -> p.getPrice() * p.getQuantity())
                .sum();
    }
}
