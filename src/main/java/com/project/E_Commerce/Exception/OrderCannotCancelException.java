package com.project.E_Commerce.Exception;

public class OrderCannotCancelException extends RuntimeException {
    public OrderCannotCancelException(String message) {
        super(message);
    }
}
