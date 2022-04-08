package com.rishabh.liveorderboard.exception;

public class OrderAlreadyPresentException extends RuntimeException {
    public OrderAlreadyPresentException(String message) {
        super(message);
    }
}
