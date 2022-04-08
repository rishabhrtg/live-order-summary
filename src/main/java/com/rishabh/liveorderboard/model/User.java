package com.rishabh.liveorderboard.model;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class User {

    private final String userId;

    public User(@NonNull final String userId) {
        this.userId = userId;
    }
}
