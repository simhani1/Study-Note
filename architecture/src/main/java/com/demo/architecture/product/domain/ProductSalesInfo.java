package com.demo.architecture.product.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductSalesInfo {

    private SalesStatus status;

    @Builder
    public ProductSalesInfo() {
        this.status = SalesStatus.WAITING;
    }

    @Builder(builderMethodName = "withStatus")
    public ProductSalesInfo(SalesStatus status) {
        this.status = status;
    }
}
