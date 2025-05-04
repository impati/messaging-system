package com.example.impati.messaging_system.adaptor.in.rest.response;

import java.time.LocalDateTime;

public record MessageResponse(
        LocalDateTime createdAt,

        Object data
) {

}
