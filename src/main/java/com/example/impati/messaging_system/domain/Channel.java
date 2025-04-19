package com.example.impati.messaging_system.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Channel {

    private final String channelId;
    private final String channelName;
    private List<Message> queue;
    private Long offset;

    private Channel(String channelId, String channelName) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.queue = new ArrayList<>();
        this.offset = 0L;
    }

    public static Channel create(String channelName) {
        return new Channel(UUID.randomUUID().toString().substring(0, 5), channelName);
    }

    public Boolean isSame(String channelName) {
        return this.channelName.equals(channelName);
    }

    public void insert(Message message) {
        queue.add(message);
    }

    public Long consumerOffset() {
        return offset + queue.size();
    }

    public List<Message> pop(Long offset) {
        return new ArrayList<>(queue.subList((int) (offset - this.offset), queue.size()));
    }

    public void updateQueue(List<Consumer> consumers) {
        long begin = consumers.stream()
                .mapToLong(it -> it.getOffset() - this.offset)
                .min()
                .orElse(0L);

        this.queue = new ArrayList<>(queue.subList((int) begin, queue.size()));
        this.offset = begin;
    }
}
