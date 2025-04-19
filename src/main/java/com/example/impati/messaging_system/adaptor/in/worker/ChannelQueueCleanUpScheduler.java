package com.example.impati.messaging_system.adaptor.in.worker;

import com.example.impati.messaging_system.domain.Channel;
import com.example.impati.messaging_system.domain.ChannelRepository;
import com.example.impati.messaging_system.domain.Consumer;
import com.example.impati.messaging_system.domain.ConsumerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelQueueCleanUpScheduler {

    private final ChannelRepository channelRepository;
    private final ConsumerRepository consumerRepository;

    @Scheduled(fixedDelay = 60_000)
    public void updateQueue() {
        List<Channel> channels = channelRepository.findAll();

        for (Channel channel : channels) {
            List<Consumer> consumers = consumerRepository.getByChannel(channel);
            channel.updateQueue(consumers);
        }
    }
}
