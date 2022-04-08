package com.rishabh.liveorderboard.exception;

public class OrderTypeNotSupportedException extends RuntimeException {
    public OrderTypeNotSupportedException(String message) {
        super(message);
    }
}
