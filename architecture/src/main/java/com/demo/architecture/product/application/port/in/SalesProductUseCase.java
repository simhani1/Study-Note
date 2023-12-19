package com.demo.architecture.product.application.port.in;

import com.demo.architecture.product.application.command.StartSalesProductCommand;

public interface SalesProductUseCase {

    void startSalesProduct(StartSalesProductCommand cmd);
}
