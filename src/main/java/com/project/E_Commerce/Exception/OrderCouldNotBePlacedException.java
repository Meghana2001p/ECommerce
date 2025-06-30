package com.project.E_Commerce.Exception;

public class OrderCouldNotBePlacedException extends RuntimeException {
    public OrderCouldNotBePlacedException(String message) {
        super(message);
    }
}
