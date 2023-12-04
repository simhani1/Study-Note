package com.demo.architecture.order.repository.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long id;

    @Column(name = "orderer_id", nullable = false)
    private Long ordererId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderEntity")
    @Column(name = "orderer_id", nullable = false)
    private List<OrderedProductEntity> orderedProductEntities = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;
}
