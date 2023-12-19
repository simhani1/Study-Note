package com.demo.architecture.product.application.service;

import com.demo.architecture.product.application.command.StartSalesProductCommand;
import com.demo.architecture.product.application.port.in.SalesProductUseCase;
import com.demo.architecture.product.application.port.out.LoadProductPort;
import com.demo.architecture.product.application.port.out.SaveProductPort;
import com.demo.architecture.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class SalesProductService implements SalesProductUseCase {

    private final LoadProductPort loadProductPort;
    private final SaveProductPort saveProductPort;

    @Override
    public void startSalesProduct(StartSalesProductCommand cmd) {
        Product product = loadProductPort.findById(cmd.getProductId());
        product.startSales();
        saveProductPort.save(product);
    }
}
