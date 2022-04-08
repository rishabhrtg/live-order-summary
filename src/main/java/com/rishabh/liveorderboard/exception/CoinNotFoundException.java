package com.rishabh.liveorderboard.exception;

import lombok.NonNull;

public class CoinNotFoundException extends RuntimeException {
    public CoinNotFoundException(@NonNull String message) {
        super(message);
    }
}
