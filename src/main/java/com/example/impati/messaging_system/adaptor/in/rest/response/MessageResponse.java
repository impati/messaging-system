package com.example.impati.messaging_system.adaptor.in.rest.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;

public record MessageResponse(
        LocalDateTime createdAt,

        JsonNode data
) {

}
