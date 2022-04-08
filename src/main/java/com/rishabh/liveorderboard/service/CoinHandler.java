package com.rishabh.liveorderboard.service;

import com.rishabh.liveorderboard.exception.CoinAlreadExistException;
import com.rishabh.liveorderboard.exception.CoinNotFoundException;
import com.rishabh.liveorderboard.model.Coin;
import com.rishabh.liveorderboard.model.OrderEvent;
import lombok.NonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CoinHandler {

    private final ConcurrentMap<String, Coin> coinByName;

    public CoinHandler() {
        this.coinByName = new ConcurrentHashMap<>();
    }

    private void addCoin(@NonNull final Coin coin) {
        String coinName = coin.getName();
        if (coinByName.containsKey(coinName)) {
            throw new CoinAlreadExistException("Coin with name : " + coinName + "already present");
        } else {
            coinByName.put(coinName, coin);
        }
    }

    public void process(@NonNull OrderEvent orderEvent) {
        final String orderCoinName = orderEvent.getOrder().getCoinName();
        Coin existingCoin;
        try {
            existingCoin = this.getCoin(orderCoinName);


        } catch (CoinNotFoundException ex) {
            //for each coin we can decide the view we need to create
            // PriceViewProcessor must be configurable TODO
            existingCoin = new Coin(orderCoinName, new PriceViewProcessor());
            this.coinByName.putIfAbsent(orderCoinName, existingCoin);
        }
        processOrderInView(orderEvent, existingCoin);
    }

    private void processOrderInView(OrderEvent orderEvent, Coin existingCoin) {
        existingCoin.getBoardSummary().getViewProcessor().process(orderEvent);
    }

    /*private void processOrderForCoin(Coin coin, OrderEvent orderEvent) {
        coin.getBoardSummary().getOrders().add(orderEvent.getOrder().getOrderId(), orderEvent);
    }*/

    public Coin getCoin(@NonNull final String coinName) {
        if (!coinByName.containsKey(coinName)) {
            throw new CoinNotFoundException("Coin not fount with name : " + coinName);
        }
        return coinByName.get(coinName);
    }
}
