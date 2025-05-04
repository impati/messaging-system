package com.example.impati.messaging_system.domain.exception;

public class ClientNotFoundException extends IllegalArgumentException {

    public ClientNotFoundException() {
        super("클라이언트가 존재하지 않습니다.");
    }
}
