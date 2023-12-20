package com.demo.architecture.product.adapter.out.persistence;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "PRODUCT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ProductJpaEntity {

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

    public void update(ProductJpaEntity target) {
        this.productName = target.getProductName();
        this.seller = target.getSeller();
        this.status = target.getStatus();
        this.price = target.getPrice();
        this.price = target.getPrice();
    }
}
