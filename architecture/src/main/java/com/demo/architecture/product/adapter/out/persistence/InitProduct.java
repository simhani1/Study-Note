package com.demo.architecture.product.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Profile("local")
@Component
@RequiredArgsConstructor
public class InitProduct {
    private final InitProductService initProductService;

    @PostConstruct
    public void init() {
        initProductService.init();
    }

    @Component
    static class InitProductService {

        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            for (int i = 0; i < 100; i++) {
                em.persist(ProductJpaEntity.builder()
                        .productName("product" + i)
                        .price(1000 + 10 * i)
                        .quantity(999)
                        .status(SalesStatusJpa.WAITING)
                        .seller("농심")
                        .build());
            }
        }
    }

}
