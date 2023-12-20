package com.demo.architecture.product.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductSalesInfo {

    private SalesStatus status;

    public ProductSalesInfo() {
        this(SalesStatus.WAITING);
    }

    @Builder(builderMethodName = "withStatus")
    public ProductSalesInfo(SalesStatus status) {
        this.status = status;
    }
}
