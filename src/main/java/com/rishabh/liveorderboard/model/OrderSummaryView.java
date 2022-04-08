package com.rishabh.liveorderboard.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class OrderSummaryView implements View {
    //private final long orderId;
    private final BigDecimal quantity;
    private final BigDecimal price;

    public OrderSummaryView(@NonNull final BigDecimal quantity, @NonNull final BigDecimal price) {
        // this.orderId = orderId;
        this.quantity = quantity;
        this.price = price;
    }
}
