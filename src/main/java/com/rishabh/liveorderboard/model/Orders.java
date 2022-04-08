package com.rishabh.liveorderboard.model;

import com.rishabh.liveorderboard.exception.OrderAlreadyPresentException;
import com.rishabh.liveorderboard.exception.OrderNotFoundException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
public class Orders {

    private final ConcurrentMap<String, OrderEvent> orderByName;

    public Orders() {
        this.orderByName = new ConcurrentHashMap<>();
    }

    public BigDecimal getOrderQuantity() {
        return this.orderByName.entrySet().stream().map(entry -> entry.getValue().getOrder().getCoinQuantity())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String add(String orderId, OrderEvent orderEvent) {
        if (this.orderByName.containsKey(orderId))
            throw new OrderAlreadyPresentException("Order already exist with order id : " + orderId);
        orderByName.put(orderId, orderEvent);
        return orderId;
    }

    public int size() {
        return orderByName.size();
    }

    public OrderEvent remove(String orderId) {
        if (!this.orderByName.containsKey(orderId)) {
            throw new OrderNotFoundException("Order not exist for order id : " + orderId);
        }
        return this.orderByName.remove(orderId);
    }

}
