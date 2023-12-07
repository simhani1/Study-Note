package com.demo.architecture.product.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductInfo {

    private String productName;
    private String seller;
}
