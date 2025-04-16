package com.example.impati.messaging_system.adaptor.in.rest;

import com.example.impati.messaging_system.domain.Channel;
import com.example.impati.messaging_system.domain.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelRepository channelRepository;

    @PostMapping("/v1/channels")
    public ResponseEntity<Void> registerChannel(@RequestBody ChannelRequest request) {

        channelRepository.save(Channel.create(request.channelName()));
        return ResponseEntity.ok().build();
    }
}
