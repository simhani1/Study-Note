package com.demo.architecture.order.service.port.in;

import com.demo.architecture.order.service.command.PurchaseProductCommand;

public interface PostOrderUsecase {

    void purchaseProduct(PurchaseProductCommand command);
}
