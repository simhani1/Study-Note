package com.demo.architecture.product.application.port.out;

import com.demo.architecture.product.domain.Product;

public interface LoadProductPort {

    Product findById(Long productId);
}
