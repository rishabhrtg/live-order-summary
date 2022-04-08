package com.rishabh.liveorderboard.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
public class OrderEvent {

    private final Order order;
    private final OrderType orderType;
    private final OrderAction orderAction;
    private final User user;

    public OrderEvent(@NonNull final Order order, @NonNull final OrderType orderType, @NonNull final String userId, @NonNull final OrderAction orderAction) {
        this.order = order;
        this.orderType = orderType;
        this.orderAction = orderAction;
        this.user = new User(userId);
    }
}
