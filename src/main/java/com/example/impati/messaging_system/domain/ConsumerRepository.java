package com.example.impati.messaging_system.domain;

import java.util.List;

public interface ConsumerRepository {

    void save(Consumer consumer);

    Consumer getById(String consumerId);

    List<Consumer> getByChannel(Channel channel);
}
