package com.demo.architecture.product.application.service;

import com.demo.architecture.product.application.command.StartSaleProductCommand;
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
public class SalesProductService implements SalesProductUseCase {

    private final LoadProductPort loadProductPort;
    private final SaveProductPort saveProductPort;

    @Override
    public void salesProduct(StartSaleProductCommand cmd) {
        Product product = loadProductPort.findById(cmd.getProductId());
        product.startSales();
        saveProductPort.save(product);
    }
}
