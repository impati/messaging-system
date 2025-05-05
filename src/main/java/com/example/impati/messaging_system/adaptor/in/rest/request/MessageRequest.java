package com.example.impati.messaging_system.adaptor.in.rest.request;

import java.time.LocalDateTime;

public record MessageRequest(
        String id,
        LocalDateTime createdAt,
        Object data
) {

}
