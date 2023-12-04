package com.demo.architecture.order.repository.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ORDERED_PRODUCT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderedProductEntity {

    @Id
    @GeneratedValue
    @Column(name = "ordered_product_id", nullable = false)
    private Long orderedProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity orderEntity;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_quantity", nullable = false)
    private int quantity;

    @Column(name = "product_price", nullable = false)
    private int price;
}