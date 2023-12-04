package com.demo.architecture.order.controller;

import com.demo.architecture.order.controller.response.PurchaseProductRes;
import com.demo.architecture.order.service.command.PurchaseProductCommand;
import com.demo.architecture.order.service.port.in.PostOrderUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final PostOrderUsecase postOrderUsecase;

    /**
     * 상품 구매하기
     * @param memberId
     * @param productId
     * @return
     */
    @PostMapping("/purchase/{member-id}/products/{product-id}")
    public ResponseEntity<PurchaseProductRes> purchaseProduct(@PathVariable(value = "member-id") Long memberId,
                                                              @PathVariable(value = "product-id") Long productId) {
        postOrderUsecase.purchaseProduct(PurchaseProductCommand.builder()
                .memberId(memberId)
                .productId(productId)
                .build());
        return ResponseEntity.ok(new PurchaseProductRes("상품 구매를 성공했습니다."));
    }
}
