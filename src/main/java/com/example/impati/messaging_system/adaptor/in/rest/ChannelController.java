package com.example.impati.messaging_system.adaptor.in.rest;

import com.example.impati.messaging_system.adaptor.in.rest.request.ChannelRequest;
import com.example.impati.messaging_system.adaptor.in.rest.request.MessageRequest;
import com.example.impati.messaging_system.adaptor.in.rest.request.SubscribeRequest;
import com.example.impati.messaging_system.adaptor.in.rest.response.ChannelResponse;
import com.example.impati.messaging_system.adaptor.in.rest.response.ErrorResponse;
import com.example.impati.messaging_system.adaptor.in.rest.response.MessageResponse;
import com.example.impati.messaging_system.adaptor.in.rest.response.MessagesResponse;
import com.example.impati.messaging_system.adaptor.in.rest.response.SubscribeResponse;
import com.example.impati.messaging_system.domain.Channel;
import com.example.impati.messaging_system.domain.ChannelRepository;
import com.example.impati.messaging_system.domain.Client;
import com.example.impati.messaging_system.domain.ClientRepository;
import com.example.impati.messaging_system.domain.Consumer;
import com.example.impati.messaging_system.domain.ConsumerRepository;
import com.example.impati.messaging_system.domain.Message;
import com.example.impati.messaging_system.domain.exception.ChannelNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelRepository channelRepository;
    private final ClientRepository clientRepository;
    private final ConsumerRepository consumerRepository;

    /**
     * 채널 생성
     */
    @PostMapping("/v1/channels")
    public ResponseEntity<Void> createChannel(@RequestBody ChannelRequest request) {

        channelRepository.save(Channel.create(request.channelName()));
        return ResponseEntity.ok().build();
    }

    /**
     * 채널에 메시지 발행
     */
    @PostMapping("/v1/channels/{channelName}/messages-publication")
    public ResponseEntity<Void> insertMessage(@PathVariable String channelName, @RequestBody MessageRequest request) {
        Channel channel = channelRepository.getByChannelName(channelName);
        channel.insert(new Message(request.createdAt(), request.data()));
        return ResponseEntity.ok().build();
    }

    /**
     * 채널 정보 조회
     */
    @GetMapping("/v1/channels/{channelName}")
    public ResponseEntity<ChannelResponse> getMessage(@PathVariable String channelName) {
        Channel channel = channelRepository.getByChannelName(channelName);
        return ResponseEntity.ok().body(ChannelResponse.from(channel));
    }

    /**
     * 클라이언트 등록
     */
    @PostMapping("/v1/client")
    public ResponseEntity<String> registerClient(@RequestParam String clientName) {

        Client client = Client.create(clientName);
        clientRepository.save(client);

        return ResponseEntity.ok().body(client.clientId());
    }

    /**
     * 채널 구독
     */
    @PostMapping("/v1/channels/{channelName}/message-subscribe")
    public ResponseEntity<SubscribeResponse> subscribe(@PathVariable String channelName, @RequestBody SubscribeRequest request) {
        Channel channel = channelRepository.getByChannelName(channelName);
        Client client = clientRepository.getById(request.clientId());
        Consumer consumer = Consumer.create(client, channel);
        consumerRepository.save(consumer);

        return ResponseEntity.ok().body(SubscribeResponse.from(consumer));
    }

    /**
     * 메시지 소비
     */
    @GetMapping("/v1/consume/{consumerId}")
    public ResponseEntity<MessagesResponse> consume(@PathVariable String consumerId) {
        Consumer consumer = consumerRepository.getById(consumerId);
        List<Message> messages = consumer.consume();
        MessagesResponse response = new MessagesResponse(
                messages.stream().map(it -> new MessageResponse(it.createdAt(), it.data())).toList()
        );

        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(ChannelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(ChannelNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ex.getMessage()
                ));
    }
}
