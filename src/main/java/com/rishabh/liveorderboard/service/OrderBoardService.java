package com.rishabh.liveorderboard.service;

import com.rishabh.liveorderboard.exception.CoinNotFoundException;
import com.rishabh.liveorderboard.exception.OrderAlreadyPresentException;
import com.rishabh.liveorderboard.exception.ViewNotFoundException;
import com.rishabh.liveorderboard.model.*;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OrderBoardService {

    private final ViewProcessor viewProcessor;
    private final CoinHandler coinHandler;
    private final ConcurrentMap<String, OrderEvent> allOrderByOrderId;

    public OrderBoardService(@NonNull final ViewProcessor viewProcessor) {

        this.viewProcessor = viewProcessor;
        this.coinHandler = new CoinHandler();
        this.allOrderByOrderId = new ConcurrentHashMap<>();
    }

    //placeCoinOrder
    public String placeCoinOrder(@NonNull final OrderEvent orderEvent) {
        String orderId = orderEvent.getOrder().getOrderId();
        if (allOrderByOrderId.containsKey(orderId)) {
            throw new OrderAlreadyPresentException("Order already exist with id : " + orderId);
        }
        allOrderByOrderId.put(orderEvent.getOrder().getOrderId(), orderEvent);
        coinHandler.process(orderEvent);
        return orderEvent.getOrder().getOrderId();
    }

    //cancelCoinOrder
    //refactor //TODO
    public void cancelCoinOrder(@NonNull final String orderId) {

        OrderEvent newOrderEvent = buildNewOrderEvent(orderId);
        coinHandler.process(newOrderEvent);
        allOrderByOrderId.remove(orderId);
    }

    private OrderEvent buildNewOrderEvent(String orderId) {
        OrderEvent oldOrderEvent = allOrderByOrderId.get(orderId);
        Order oldOrder = oldOrderEvent.getOrder();
        Order newOrder = new Order(oldOrder.getOrderId(), oldOrder.getCoinName(), oldOrder.getCoinQuantity(), oldOrder.getCoinPrice());
        OrderEvent newOrderEvent = new OrderEvent(newOrder, oldOrderEvent.getOrderType(), oldOrderEvent.getUser().getUserId(), OrderAction.DELETE);
        return newOrderEvent;
    }

    public List<? extends View> getCoinOrderSummaryByOrderType(@NonNull final String coinName, @NonNull final String orderType, @NonNull final int sizeLimit) {

        try {
            Coin coin = coinHandler.getCoin(coinName);
            return coin.getBoardSummary().getViewProcessor().getViewByType(orderType, sizeLimit);
        } catch (CoinNotFoundException ex) {
            throw new ViewNotFoundException("view not exist for coinName : " + coinName);
        }
    }
}
