package com.example.impati.messaging_system.adaptor.in.rest.request;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;

public record MessageRequest(

        LocalDateTime createdAt,
        JsonNode data
) {

}
