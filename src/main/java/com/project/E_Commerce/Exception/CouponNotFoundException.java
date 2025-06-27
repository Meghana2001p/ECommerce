package com.project.E_Commerce.Exception;

public class CouponNotFoundException extends RuntimeException {
  public CouponNotFoundException(String message) {
    super(message);
  }
}
