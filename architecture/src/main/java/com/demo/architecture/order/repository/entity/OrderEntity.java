package com.demo.architecture.order.repository.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "ORDER_INFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id", nullable = false)
    private Long id;

    @Column(name = "orderer_id", nullable = false)
    private Long ordererId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderEntity")
    private List<OrderedProductEntity> orderedProductEntities = new ArrayList<>();

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatusEntity orderStatusEntity;
}
