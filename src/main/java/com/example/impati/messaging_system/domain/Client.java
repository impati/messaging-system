package com.example.impati.messaging_system.domain;

import java.util.UUID;

public record Client(
        String clientId,
        String clientName
) {

    public static Client create(String clientName) {
        return new Client(
                UUID.randomUUID().toString().substring(0, 5),
                clientName
        );
    }

}
