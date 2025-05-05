package com.example.impati.messaging_system.domain;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConsumerTest {

    @Test
    @DisplayName("메시지를 소비한다.")
    void consume() {
        Client client = Client.create("client");
        Channel channel = Channel.create("channel");
        Consumer consumer = Consumer.create(client, channel);
        channel.insert(new Message(
                "1",
                LocalDateTime.now(),
                JsonNodeFactory.instance.numberNode(3)
        ));
        channel.insert(new Message(
                "2",
                LocalDateTime.now(),
                JsonNodeFactory.instance.numberNode(2)
        ));
        channel.insert(new Message(
                "3",
                LocalDateTime.now(),
                JsonNodeFactory.instance.numberNode(1)
        ));

        List<Message> messages = consumer.consume();

        assertThat(messages).hasSize(3);
    }

    @Test
    @DisplayName("메시지를 소비한다.")
    void consume2() {
        Client client = Client.create("client");
        Channel channel = Channel.create("channel");
        channel.insert(new Message(
                "4",
                LocalDateTime.now(),
                JsonNodeFactory.instance.numberNode(1)
        ));
        Consumer consumer = Consumer.create(client, channel);
        channel.insert(new Message(
                "5",
                LocalDateTime.now(),
                JsonNodeFactory.instance.numberNode(2)
        ));

        List<Message> messages = consumer.consume();

        assertThat(messages).hasSize(1);
    }

    @Test
    @DisplayName("메시지를 소비한다.")
    void consume3() {
        Client client = Client.create("client");
        Channel channel = Channel.create("channel");
        channel.insert(new Message(
                "1",
                LocalDateTime.now(),
                JsonNodeFactory.instance.numberNode(1)
        ));
        Consumer consumer = Consumer.create(client, channel);
        channel.insert(new Message(
                "2",
                LocalDateTime.now(),
                JsonNodeFactory.instance.numberNode(2)
        ));
        List<Message> beforeMessage = consumer.consume();

        channel.insert(new Message(
                "3",
                LocalDateTime.now(),
                JsonNodeFactory.instance.numberNode(3)
        ));
        channel.insert(new Message(
                "4",
                LocalDateTime.now(),
                JsonNodeFactory.instance.numberNode(4)
        ));

        List<Message> messages = consumer.consume();

        assertThat(beforeMessage).hasSize(1);
        assertThat(messages).hasSize(2);
    }
}
