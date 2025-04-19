package com.example.impati.messaging_system.adaptor.in.rest.response;

import com.example.impati.messaging_system.domain.Consumer;

public record SubscribeResponse(
        String consumerId,
        String clientId,
        ChannelResponse channelResponse
) {

    public static SubscribeResponse from(Consumer consumer) {
        return new SubscribeResponse(
                consumer.getConsumerId(),
                consumer.getClient().clientId(),
                ChannelResponse.from(consumer.getChannel())
        );
    }
}
