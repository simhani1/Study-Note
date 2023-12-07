package com.demo.architecture.product.adapter.out.mapper;

import com.demo.architecture.product.adapter.out.persistence.ProductJpaEntity;
import com.demo.architecture.product.adapter.out.persistence.SalesStatusJpa;
import com.demo.architecture.product.domain.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductMapper {

    public Product toDomain(ProductJpaEntity productJpaEntity) {
        return Product.withId()
                .productId(ProductId.builder()
                        .productId(productJpaEntity.getId())
                        .build())
                .productInfo(ProductInfo.builder()
                        .productName(productJpaEntity.getProductName())
                        .seller(productJpaEntity.getSeller())
                        .build())
                .productMetaInfo(ProductMetaInfo.builder()
                        .price(productJpaEntity.getPrice())
                        .quantity(productJpaEntity.getQuantity())
                        .build())
                .productSalesInfo(ProductSalesInfo.withStatus()
                        .status(SalesStatus.valueOf(productJpaEntity.getStatus().toString()))
                        .build())
                .build();
    }

    public ProductJpaEntity toJpaEntity(Product product) {
        return ProductJpaEntity.builder()
                .productName(product.getProductInfo().getProductName())
                .seller(product.getProductInfo().getSeller())
                .price(product.getProductMetaInfo().getPrice())
                .quantity(product.getProductMetaInfo().getQuantity())
                .status(SalesStatusJpa.valueOf(product.getProductSalesInfo().getStatus().toString()))
                .build();
    }
}
