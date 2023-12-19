package com.demo.architecture.product.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StartSalesProductCommand {

    private Long productId;
}
