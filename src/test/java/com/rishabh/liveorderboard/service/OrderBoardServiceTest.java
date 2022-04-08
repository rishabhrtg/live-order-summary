package com.rishabh.liveorderboard.service;

import com.rishabh.liveorderboard.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class OrderBoardServiceTest {

    OrderBoardService orderBoardService;

    @Test
    public void placeTwoCoinOrderAndGetListBuyTest() {

        orderBoardService = new OrderBoardService(new PriceViewProcessor());
        OrderEvent orderEvent1 = new OrderEvent(new Order("1", "ETH", BigDecimal.valueOf(2), BigDecimal.valueOf(10)), OrderType.BUY, "123", OrderAction.ADD);
        orderBoardService.placeCoinOrder(orderEvent1);
        OrderEvent orderEvent2 = new OrderEvent(new Order("3", "ETH", BigDecimal.valueOf(5), BigDecimal.valueOf(10)), OrderType.BUY, "123", OrderAction.ADD);
        orderBoardService.placeCoinOrder(orderEvent2);

        List<OrderSummaryView> orderSummaryViews = (List<OrderSummaryView>) orderBoardService.getCoinOrderSummaryByOrderType("ETH", "BUY", 10);
        assertSame(orderSummaryViews.size(), 1);
        double actual = orderSummaryViews.stream().map(OrderSummaryView::getQuantity).mapToDouble(BigDecimal::doubleValue).sum();
        double expected = 7.0;
        assertEquals(expected, actual);
    }

    @Test
    public void placeCoinOrderTwoBuyOneSellGetListTest() {
        orderBoardService = new OrderBoardService(new PriceViewProcessor());

        OrderEvent orderEvent1 = new OrderEvent(new Order("1", "ETH", BigDecimal.valueOf(2), BigDecimal.valueOf(10)), OrderType.SELL, "123", OrderAction.ADD);
        orderBoardService.placeCoinOrder(orderEvent1);
        OrderEvent orderEvent2 = new OrderEvent(new Order("3", "ETH", BigDecimal.valueOf(5), BigDecimal.valueOf(10)), OrderType.SELL, "123", OrderAction.ADD);
        orderBoardService.placeCoinOrder(orderEvent2);

        orderBoardService.getCoinOrderSummaryByOrderType("ETH", "SELL", 4)
                .forEach(view -> {
                    OrderSummaryView orderSummaryView = (OrderSummaryView) view;
                    System.out.println(orderSummaryView);
                });

        orderBoardService.cancelCoinOrder("1");

        List<OrderSummaryView> orderSummaryViews = (List<OrderSummaryView>) orderBoardService.getCoinOrderSummaryByOrderType("ETH", "SELL", 10);
        assertSame(orderSummaryViews.size(), 1);
        double actual = orderSummaryViews.stream().map(OrderSummaryView::getQuantity).mapToDouble(BigDecimal::doubleValue).sum();
        double expected = 5.0;
        assertEquals(expected, actual);
    }

    @Test
    public void cancelCoinOrder() {
    }

    @Test
    public void getCoinOrderSummaryByOrderType() {

        orderBoardService = new OrderBoardService(new PriceViewProcessor());
        OrderEvent orderEvent1 = new OrderEvent(new Order("1", "ETH", BigDecimal.valueOf(2), BigDecimal.valueOf(10)), OrderType.SELL, "123", OrderAction.ADD);
        orderBoardService.placeCoinOrder(orderEvent1);
        OrderEvent orderEvent2 = new OrderEvent(new Order("2", "ETH", BigDecimal.valueOf(5), BigDecimal.valueOf(10)), OrderType.SELL, "123", OrderAction.ADD);
        orderBoardService.placeCoinOrder(orderEvent2);

        OrderEvent orderEvent3 = new OrderEvent(new Order("3", "ETH", BigDecimal.valueOf(3), BigDecimal.valueOf(3)), OrderType.SELL, "123", OrderAction.ADD);
        orderBoardService.placeCoinOrder(orderEvent3);
        OrderEvent orderEvent4 = new OrderEvent(new Order("4", "ETH", BigDecimal.valueOf(4), BigDecimal.valueOf(4)), OrderType.SELL, "123", OrderAction.ADD);
        orderBoardService.placeCoinOrder(orderEvent4);

        OrderEvent orderEvent5 = new OrderEvent(new Order("5", "ETH", BigDecimal.valueOf(5), BigDecimal.valueOf(5)), OrderType.SELL, "123", OrderAction.ADD);
        orderBoardService.placeCoinOrder(orderEvent5);
        OrderEvent orderEvent6 = new OrderEvent(new Order("6", "ETH", BigDecimal.valueOf(6), BigDecimal.valueOf(6)), OrderType.SELL, "123", OrderAction.ADD);
        orderBoardService.placeCoinOrder(orderEvent6);

        orderBoardService.getCoinOrderSummaryByOrderType("ETH", "SELL", 4)
                .forEach(view -> {
                    OrderSummaryView orderSummaryView = (OrderSummaryView) view;
                    System.out.println(orderSummaryView);
                });
        System.out.println("--------------------");

        List<OrderSummaryView> orderSummaryViews = (List<OrderSummaryView>) orderBoardService.getCoinOrderSummaryByOrderType("ETH", "SELL", 10);
        orderSummaryViews.forEach(view -> {
            OrderSummaryView orderSummaryView = view;
            System.out.println(orderSummaryView);
        });

        double actual = orderSummaryViews.stream().map(OrderSummaryView::getQuantity).mapToDouble(BigDecimal::doubleValue).sum();
        double expected = 25.0;
        assertEquals(expected, actual);

    }
}