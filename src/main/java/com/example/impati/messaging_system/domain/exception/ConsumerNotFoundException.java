package com.example.impati.messaging_system.domain.exception;

public class ConsumerNotFoundException extends IllegalArgumentException {

    public ConsumerNotFoundException() {
        super("구독하지 않았습니다.");
    }
}
