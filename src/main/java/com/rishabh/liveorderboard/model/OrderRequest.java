package com.rishabh.liveorderboard.model;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class OrderRequest {
    private final String orderId;
    private final String coinName;
    private final String userId;

    public OrderRequest(@NonNull final String orderId, @NonNull final String coinName, @NonNull final String userId) {
        this.coinName = coinName;
        this.orderId = orderId;
        this.userId = userId;
    }
}
