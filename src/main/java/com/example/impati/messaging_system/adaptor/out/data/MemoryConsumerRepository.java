package com.example.impati.messaging_system.adaptor.out.data;

import com.example.impati.messaging_system.domain.Channel;
import com.example.impati.messaging_system.domain.Client;
import com.example.impati.messaging_system.domain.Consumer;
import com.example.impati.messaging_system.domain.ConsumerRepository;
import com.example.impati.messaging_system.domain.exception.ConsumerNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class MemoryConsumerRepository implements ConsumerRepository {

    private final Map<String, Consumer> store = new HashMap<>();

    @Override
    public void save(final Consumer consumer) {
        store.put(consumer.getConsumerId(), consumer);
    }

    @Override
    public Consumer getById(final String consumerId) {
        return store.get(consumerId);
    }

    @Override
    public Consumer getByClientAndChannel(final Client client, final Channel channel) {
        return store.values()
                .stream()
                .filter(it -> it.getClient().equals(client))
                .filter(it -> it.getChannel().equals(channel))
                .findFirst()
                .orElseThrow(ConsumerNotFoundException::new);
    }

    @Override
    public List<Consumer> getByChannel(final Channel channel) {
        return store.values().stream().filter(it -> it.getChannel().equals(channel)).toList();
    }
}
