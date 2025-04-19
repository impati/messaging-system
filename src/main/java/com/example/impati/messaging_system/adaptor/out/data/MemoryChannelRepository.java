package com.example.impati.messaging_system.adaptor.out.data;

import com.example.impati.messaging_system.domain.Channel;
import com.example.impati.messaging_system.domain.ChannelRepository;
import com.example.impati.messaging_system.domain.exception.ChannelNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * 단일 서버 기준
 */
@Component
public class MemoryChannelRepository implements ChannelRepository {

    private final Map<String, Channel> store = new HashMap<>();

    @Override
    public void save(Channel channel) {
        if (store.values().stream().anyMatch(it -> it.isSame(channel.getChannelName()))) {
            throw new IllegalArgumentException("채널이름을 중복일 수 없습니다.");
        }

        store.put(channel.getChannelId(), channel);
    }

    @Override
    public Optional<Channel> findById(final String channelId) {
        return Optional.of(store.get(channelId));
    }

    @Override
    public Channel getByChannelName(final String channelName) {
        return store.values()
                .stream()
                .filter(it -> it.isSame(channelName))
                .findFirst()
                .orElseThrow(ChannelNotFoundException::new);
    }

    @Override
    public List<Channel> findAll() {
        return store.values().stream().toList();
    }
}
