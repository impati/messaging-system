package com.example.impati.messaging_system.domain;

import java.util.UUID;
import lombok.Getter;

@Getter
public class Channel {

    private final String channelId;
    private final String channelName;

    private Channel(String channelId, String channelName) {
        this.channelId = channelId;
        this.channelName = channelName;
    }

    public static Channel create(String channelName) {
        return new Channel(UUID.randomUUID().toString().substring(0, 5), channelName);
    }
}
