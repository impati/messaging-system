package com.example.impati.messaging_system.domain;

import java.util.Optional;

public interface ChannelRepository {

    Channel save(Channel channel);

    Optional<Channel> findById(String channelId);

    Optional<Channel> findByChannelName(String channelName);
}
