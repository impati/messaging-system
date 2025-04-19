package com.example.impati.messaging_system.domain;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository {

    void save(Channel channel);

    Optional<Channel> findById(String channelId);

    Channel getByChannelName(String channelName);
    
    List<Channel> findAll();
}
