package com.demo.architecture.product.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Product {

    private ProductId productId;
    private ProductInfo productInfo;
    private ProductSalesInfo productSalesInfo;
    private ProductMetaInfo productMetaInfo;

    @Builder
    public Product(ProductInfo productInfo, ProductSalesInfo productSalesInfo, ProductMetaInfo productMetaInfo) {
        this.productInfo = productInfo;
        this.productSalesInfo = new ProductSalesInfo();
        this.productMetaInfo = productMetaInfo;
    }

    @Builder(builderMethodName = "withId")
    public Product(ProductId productId, ProductInfo productInfo, ProductSalesInfo productSalesInfo, ProductMetaInfo productMetaInfo) {
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
