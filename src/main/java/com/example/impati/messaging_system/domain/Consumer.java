package com.example.impati.messaging_system.domain;

import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Consumer {

    private final String consumerId;
    private final Client client;
    private final Channel channel;
    private Long offset;

    public Consumer(String consumerId, Client client, Channel channel, Long offset) {
        this.consumerId = consumerId;
        this.client = client;
        this.channel = channel;
        this.offset = offset;
    }

    public static Consumer create(Client client, Channel channel) {
        return new Consumer(
                UUID.randomUUID().toString().substring(0, 5),
                client,
                channel,
                channel.consumerOffset()
        );
    }

    /**
     * 동시에 접근하면 안되는 임계 영역
     */
    public List<Message> consume() {
        List<Message> messages = channel.pop(offset);
        offset += messages.size();

        return messages;
    }
}
