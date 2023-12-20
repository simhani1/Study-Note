package com.demo.architecture.product.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Product {

    private Long productId;
    private ProductInfo productInfo;
    private ProductSalesInfo productSalesInfo;
    private ProductMetaInfo productMetaInfo;

    @Builder(builderMethodName = "withId")
    public Product(Long productId, ProductInfo productInfo, ProductSalesInfo productSalesInfo, ProductMetaInfo productMetaInfo) {
        this.productId = productId;
        this.productInfo = productInfo;
        this.productSalesInfo = productSalesInfo;
        this.productMetaInfo = productMetaInfo;
    }

    public void startSales() {
        this.productSalesInfo = ProductSalesInfo.withStatus().status(SalesStatus.SALES).build();
    }

    public void stopSales() {
        this.productSalesInfo = ProductSalesInfo.withStatus().status(SalesStatus.SALES_DISCONTINUED).build();
    }
}
