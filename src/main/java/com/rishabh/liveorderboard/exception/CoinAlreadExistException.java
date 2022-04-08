package com.rishabh.liveorderboard.exception;

public class CoinAlreadExistException extends RuntimeException {
    public CoinAlreadExistException(String message) {
        super(message);
    }
}
