package com.demo.architecture.product.adapter.in.app;

import com.demo.architecture.product.adapter.in.app.response.StartSaleProductAppRes;
import com.demo.architecture.product.application.command.StartSalesProductCommand;
import com.demo.architecture.product.application.port.in.SalesProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/products")
class ProductAppController {

    private final SalesProductUseCase salesProductUsecase;

    /**
     * 상품 판매 시작하기
     * @param productId
     * @return
     */
    @PostMapping("/sale/{product-id}")
    public ResponseEntity<StartSaleProductAppRes> startSalesProduct(@PathVariable(value = "product-id") Long productId) {
        salesProductUsecase.startSalesProduct(StartSalesProductCommand.builder()
                .productId(productId)
                .build());
        return ResponseEntity.ok(new StartSaleProductAppRes("[APP] " + productId + "번 상품 판매가 시작되었습니다."));
    }
}
