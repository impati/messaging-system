package com.example.impati.messaging_system.adaptor.in.rest.response;

import com.example.impati.messaging_system.domain.Channel;
import java.util.List;

public record ChannelResponse(
        String channelId,
        String channelName,
        List<MessageResponse> queue
) {

    public static ChannelResponse from(Channel channel) {
        return new ChannelResponse(
                channel.getChannelId(),
                channel.getChannelName(),
                channel.getQueue().stream().map(it -> new MessageResponse(it.createdAt(), it.data())).toList()
        );
    }
}
