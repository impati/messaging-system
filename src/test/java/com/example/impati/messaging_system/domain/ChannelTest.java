package com.example.impati.messaging_system.domain;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChannelTest {

    @Test
    @DisplayName("큐 업데이트")
    void updateQueue() {
        Client client = Client.create("client");
        Channel channel = Channel.create("channel");
        Consumer consumerA = Consumer.create(client, channel);
        channel.insert(new Message(LocalDateTime.now(), JsonNodeFactory.instance.numberNode(1)));
        channel.insert(new Message(LocalDateTime.now(), JsonNodeFactory.instance.numberNode(2)));
        Consumer consumerB = Consumer.create(client, channel);
        channel.insert(new Message(LocalDateTime.now(), JsonNodeFactory.instance.numberNode(3)));
        consumerA.consume();
        consumerB.consume();
        channel.insert(new Message(LocalDateTime.now(), JsonNodeFactory.instance.numberNode(4)));
        consumerA.consume();
        channel.insert(new Message(LocalDateTime.now(), JsonNodeFactory.instance.numberNode(5)));

        channel.updateQueue(List.of(consumerA, consumerB));

        assertThat(channel.consumerOffset()).isEqualTo(5);
        assertThat(channel.getQueue()).hasSize(2);
        assertThat(channel.getOffset()).isEqualTo(3);
    }

    @Test
    @DisplayName("큐 업데이트")
    void updateQueue2() {
        Client client = Client.create("client");
        Channel channel = Channel.create("channel");
        Consumer consumerA = Consumer.create(client, channel);
        channel.insert(new Message(LocalDateTime.now(), JsonNodeFactory.instance.numberNode(1)));
        channel.insert(new Message(LocalDateTime.now(), JsonNodeFactory.instance.numberNode(2)));
        Consumer consumerB = Consumer.create(client, channel);
        channel.insert(new Message(LocalDateTime.now(), JsonNodeFactory.instance.numberNode(3)));
        consumerA.consume();
        consumerB.consume();
        channel.insert(new Message(LocalDateTime.now(), JsonNodeFactory.instance.numberNode(4)));
        consumerA.consume();
        channel.insert(new Message(LocalDateTime.now(), JsonNodeFactory.instance.numberNode(5)));
        channel.updateQueue(List.of(consumerA, consumerB));

        Consumer consumerC = Consumer.create(client, channel);
        channel.insert(new Message(LocalDateTime.now(), JsonNodeFactory.instance.numberNode(5)));

        assertThat(consumerA.consume()).hasSize(2);
        assertThat(consumerB.consume()).hasSize(3);
        assertThat(consumerC.consume()).hasSize(1);
    }
}
