package com.demo.architecture.product.adapter.out.persistence;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "PRODUCT")
public class ProductJpaEntity {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_seller")
    private String seller;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_sales_status")
    private SalesStatusJpa status;

    @Column(name = "product_price")
    private int price;

    @Column(name = "product_quantity")
    private int quantity;

    @Builder
    public ProductJpaEntity(String productName, String seller, SalesStatusJpa status, int price, int quantity) {
        this.productName = productName;
        this.seller = seller;
        this.status = status;
        this.price = price;
        this.quantity = quantity;
    }
}
