package com.rishabh.liveorderboard.service;

import com.rishabh.liveorderboard.model.OrderEvent;
import com.rishabh.liveorderboard.model.View;
import lombok.NonNull;

import java.util.List;

public interface ViewProcessor {

    List<? extends View> getViewByType(String orderType, @NonNull int sizeLimit);

    void process(OrderEvent orderEvent);
}
