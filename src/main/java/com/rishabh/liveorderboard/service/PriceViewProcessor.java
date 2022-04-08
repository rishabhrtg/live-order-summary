package com.rishabh.liveorderboard.service;

import com.rishabh.liveorderboard.exception.OrderTypeNotSupportedException;
import com.rishabh.liveorderboard.model.*;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class PriceViewProcessor implements ViewProcessor {

    ConcurrentMap<BigDecimal, Orders> buyOrderByPrice;
    ConcurrentMap<BigDecimal, Orders> sellOrderByPrice;

    public PriceViewProcessor() {
        buyOrderByPrice = new ConcurrentSkipListMap<>(Collections.reverseOrder());
        sellOrderByPrice = new ConcurrentSkipListMap<>();
    }

    private List<OrderSummaryView> getBuyOrderView(int sizeLimit) {

        return this.buyOrderByPrice.keySet().stream()
                .map(price -> {
                    BigDecimal orderQuantity = buyOrderByPrice.get(price).getOrderQuantity();
                    return new OrderSummaryView(orderQuantity, price);
                }).limit(sizeLimit)
                .collect(Collectors.toList());
    }

    private List<OrderSummaryView> getSellOrderView(int sizeLimit) {

        return this.sellOrderByPrice.keySet().stream()
                .map(price -> {
                    BigDecimal orderQuantity = sellOrderByPrice.get(price).getOrderQuantity();
                    return new OrderSummaryView(orderQuantity, price);
                }).limit(sizeLimit)
                .collect(Collectors.toList());
    }

    @Override
    public List<? extends View> getViewByType(String orderType, @NonNull final int sizeLimit) {
        List<? extends View> viewList;
        if (OrderType.BUY.toString().equalsIgnoreCase(orderType))
            viewList = getBuyOrderView(sizeLimit);
        else if (OrderType.SELL.toString().equalsIgnoreCase(orderType))
            viewList = getSellOrderView(sizeLimit);
        else
            throw new OrderTypeNotSupportedException("View not supported for order type : " + orderType);

        return viewList;
    }

    @Override
    public void process(OrderEvent orderEvent) {
        if (orderEvent.getOrderType() == OrderType.BUY) {
            update(buyOrderByPrice, orderEvent);
        } else if (orderEvent.getOrderType() == OrderType.SELL) {
            update(sellOrderByPrice, orderEvent);
        }
    }

    private void update(ConcurrentMap<BigDecimal, Orders> priceViewMap, OrderEvent orderEvent) {
        if (orderEvent.getOrderAction() == OrderAction.ADD) {
            this.add(priceViewMap, orderEvent);
        } else if (orderEvent.getOrderAction() == OrderAction.DELETE) {
            this.remove(priceViewMap, orderEvent);
        }
    }

    private void remove(ConcurrentMap<BigDecimal, Orders> priceViewMap, OrderEvent orderEvent) {

        BigDecimal price = orderEvent.getOrder().getCoinPrice();
        Orders orders = priceViewMap.get(price);
        if (orders.size() <= 1) {
            priceViewMap.remove(price);
        }
        orders.remove(orderEvent.getOrder().getOrderId());
    }

    private void add(ConcurrentMap<BigDecimal, Orders> priceViewMap, OrderEvent orderEvent) {
        final BigDecimal price = orderEvent.getOrder().getCoinPrice();
        final String orderId = orderEvent.getOrder().getOrderId();
        if (!priceViewMap.containsKey(price)) {
            Orders coinOrders = new Orders();
            coinOrders.add(orderId, orderEvent);
            priceViewMap.putIfAbsent(price, coinOrders);
        } else {
            priceViewMap.get(price).add(orderId, orderEvent);
        }
    }
}
