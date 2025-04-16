package com.example.impati.messaging_system.adaptor.out.data;

import com.example.impati.messaging_system.domain.Channel;
import com.example.impati.messaging_system.domain.ChannelRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MemoryChannelRepository implements ChannelRepository {

    private final Map<String, Channel> store = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        return store.put(channel.channelId(), channel);
    }

    @Override
    public Optional<Channel> findById(final String channelId) {
        return Optional.of(store.get(channelId));
    }

    @Override
    public Optional<Channel> findByChannelName(final String channelName) {
        return store.values()
                .stream()
                .filter(it -> it.channelName().equals(channelName))
                .findFirst();
    }
}
