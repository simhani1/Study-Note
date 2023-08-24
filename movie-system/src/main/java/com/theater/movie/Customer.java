package com.theater.movie;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {

    private String name;
    private String id;

    public Customer(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
