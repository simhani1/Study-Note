package com.demo.architecture.order.service.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PurchaseProductCommand {

    private Long memberId;
    private Long productId;
}
