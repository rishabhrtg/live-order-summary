package com.rishabh.liveorderboard.model;

import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

@Getter
public class Order {
    private String orderId;
    private String coinName;
    private BigDecimal coinQuantity;
    private BigDecimal coinPrice;

    public Order(final String orderId, @NonNull final String coinName, BigDecimal coinQuantity, final BigDecimal coinPrice) {
        this.orderId = orderId;
        this.coinName = coinName;
        this.coinQuantity = coinQuantity;
        this.coinPrice = coinPrice;
    }

}
