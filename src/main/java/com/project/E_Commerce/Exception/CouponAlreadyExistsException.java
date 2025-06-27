package com.project.E_Commerce.Exception;

public class CouponAlreadyExistsException extends RuntimeException {
    public CouponAlreadyExistsException(String message) {
        super(message);
    }
}
