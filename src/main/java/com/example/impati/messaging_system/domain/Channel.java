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

    /**
     * 현재 보내는 메시지가 이전에 받은 메시지였다면 insert 하지 않음
     * 하지만 , updateQueuing 작업이 있기 때문에 전체 시간동안 같은 메시지가 단 한번도 존재하지 않을거라는 보장은 할 수 없음.
     * 따라서 동일한 메시지가 소비될 수 있다는 것은 컨슈머에서도 인지하고 있어야함.
     */
    public void insert(Message message) {
        if (queue.contains(message)) {
            return;
        }

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
