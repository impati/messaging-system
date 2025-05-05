package com.example.impati.messaging_system.adaptor.in.rest;

import com.example.impati.messaging_system.adaptor.in.rest.request.ChannelRequest;
import com.example.impati.messaging_system.adaptor.in.rest.request.MessageRequest;
import com.example.impati.messaging_system.adaptor.in.rest.request.SubscribeRequest;
import com.example.impati.messaging_system.adaptor.in.rest.response.ChannelResponse;
import com.example.impati.messaging_system.adaptor.in.rest.response.ErrorResponse;
import com.example.impati.messaging_system.adaptor.in.rest.response.MessageResponse;
import com.example.impati.messaging_system.adaptor.in.rest.response.MessagesResponse;
import com.example.impati.messaging_system.adaptor.in.rest.response.PublicationResponse;
import com.example.impati.messaging_system.adaptor.in.rest.response.SubscribeResponse;
import com.example.impati.messaging_system.domain.Channel;
import com.example.impati.messaging_system.domain.ChannelRepository;
import com.example.impati.messaging_system.domain.Client;
import com.example.impati.messaging_system.domain.ClientRepository;
import com.example.impati.messaging_system.domain.Consumer;
import com.example.impati.messaging_system.domain.ConsumerRepository;
import com.example.impati.messaging_system.domain.Message;
import com.example.impati.messaging_system.domain.exception.ChannelNotFoundException;
import com.example.impati.messaging_system.domain.exception.ClientNotFoundException;
import com.example.impati.messaging_system.domain.exception.ConsumerNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
        log.info("createChannel request  = {}", request);
        channelRepository.save(Channel.create(request.channelName()));
        return ResponseEntity.ok().build();
    }

    /**
     * 채널에 메시지 발행
     */
    @PostMapping("/v1/channels/{channelName}/messages-publication")
    public ResponseEntity<PublicationResponse> insertMessage(@PathVariable String channelName, @RequestBody MessageRequest request) {
        log.info("insertMessage channelName = {} , request  = {}", channelName, request);
        Channel channel = channelRepository.getByChannelName(channelName);
        Message message = new Message(request.id(), request.createdAt(), request.data());
        channel.insert(message);
        return ResponseEntity.ok(new PublicationResponse(message.id()));
    }

    /**
     * 채널 정보 조회
     */
    @GetMapping("/v1/channels/{channelName}")
    public ResponseEntity<ChannelResponse> getMessage(@PathVariable String channelName) {
        log.info("getMessage channelName = {}", channelName);
        Channel channel = channelRepository.getByChannelName(channelName);
        return ResponseEntity.ok().body(ChannelResponse.from(channel));
    }

    /**
     * 클라이언트 등록
     */
    @PostMapping("/v1/client")
    public ResponseEntity<String> registerClient(@RequestParam String clientName) {
        log.info("registerClient clientName = {}", clientName);
        Client client = Client.create(clientName);
        clientRepository.save(client);
        log.info("clientId = {}", client.clientId());

        return ResponseEntity.ok().body(client.clientId());
    }

    /**
     * 클라이언트 조회
     */
    @GetMapping("/v1/client")
    public ResponseEntity<String> getClient(@RequestParam String clientName) {
        log.info("getClient clientName = {}", clientName);
        Client client = clientRepository.getByClientName(clientName);

        return ResponseEntity.ok().body(client.clientId());
    }

    /**
     * 채널 구독
     */
    @PostMapping("/v1/channels/{channelName}/message-subscribe")
    public ResponseEntity<SubscribeResponse> subscribe(@PathVariable String channelName, @RequestBody SubscribeRequest request) {
        log.info("subscribe channelName = {} , request = {}", channelName, request);

        Channel channel = channelRepository.getByChannelName(channelName);
        Client client = clientRepository.getById(request.clientId());
        Consumer consumer = Consumer.create(client, channel);
        consumerRepository.save(consumer);

        return ResponseEntity.ok().body(SubscribeResponse.from(consumer));
    }

    /**
     * 채널 구독 정보 조회
     */
    @GetMapping("/v1/channels/{channelName}/message-subscribe")
    public ResponseEntity<SubscribeResponse> getSubscribe(@PathVariable String channelName, @RequestParam String clientId) {
        log.info("getSubscribe channelName = {} , clientId = {}", channelName, clientId);
        Channel channel = channelRepository.getByChannelName(channelName);
        Client client = clientRepository.getById(clientId);
        Consumer consumer = consumerRepository.getByClientAndChannel(client, channel);

        return ResponseEntity.ok().body(SubscribeResponse.from(consumer));
    }

    /**
     * 메시지 소비
     */
    @GetMapping("/v1/consume/{consumerId}")
    public ResponseEntity<MessagesResponse> consume(@PathVariable String consumerId) {
        log.info("consume consumerId = {}", consumerId);
        Consumer consumer = consumerRepository.getById(consumerId);
        List<Message> messages = consumer.consume();
        MessagesResponse response = new MessagesResponse(
                messages.stream().map(it -> new MessageResponse(it.createdAt(), it.data())).toList()
        );

        log.info("response = {}", response);

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

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(ClientNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(ConsumerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(ConsumerNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ex.getMessage()
                ));
    }
}
