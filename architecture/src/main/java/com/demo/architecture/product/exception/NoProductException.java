package com.demo.architecture.product.exception;

public class NoProductException extends RuntimeException {

    public NoProductException() {
    }

    public NoProductException(String message) {
        super(message);
    }

    public NoProductException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoProductException(Throwable cause) {
        super(cause);
    }
}
