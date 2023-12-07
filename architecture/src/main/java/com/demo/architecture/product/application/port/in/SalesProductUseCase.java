package com.demo.architecture.product.application.port.in;

import com.demo.architecture.product.application.command.StartSaleProductCommand;

public interface SalesProductUseCase {

    void salesProduct(StartSaleProductCommand cmd);
}
