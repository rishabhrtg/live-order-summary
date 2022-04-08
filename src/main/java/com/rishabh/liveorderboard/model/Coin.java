package com.rishabh.liveorderboard.model;

import com.rishabh.liveorderboard.service.ViewProcessor;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Coin {

    private final String name;
    private final BoardSummary boardSummary;

    public Coin(@NonNull final String name, ViewProcessor viewProcessor) {
        this.name = name;
        this.boardSummary = new BoardSummary(viewProcessor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coin coin = (Coin) o;

        return name.equals(coin.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
