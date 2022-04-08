package com.rishabh.liveorderboard.model;

import com.rishabh.liveorderboard.service.ViewProcessor;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class BoardSummary {

    private final ViewProcessor viewProcessor;

    public BoardSummary(@NonNull final ViewProcessor viewProcessor) {
        this.viewProcessor = viewProcessor;
    }
}
