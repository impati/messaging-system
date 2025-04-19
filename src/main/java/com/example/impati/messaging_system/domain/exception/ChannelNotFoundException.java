package com.example.impati.messaging_system.domain.exception;

public class ChannelNotFoundException extends IllegalArgumentException {

    public ChannelNotFoundException() {
        super("채널이 존재하지 않습니다.");
    }
}
