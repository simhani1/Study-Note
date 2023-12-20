package com.demo.architecture.product.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void statusTest() {
        // given
        Product product = Product.withId()
                .productInfo(ProductInfo.builder()
                        .seller("농심")
                        .productName("product")
                        .build())
                .productMetaInfo(ProductMetaInfo.builder()
                        .quantity(1000)
                        .price(2000)
                        .build())
                .build();

        // when
        product.startSales();

        // then
        Assertions.assertThat(product.getProductSalesInfo().getStatus().toString()).isEqualTo("SALES");

    }

}