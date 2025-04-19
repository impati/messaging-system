package com.example.impati.messaging_system.domain;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;

public record Message(LocalDateTime createdAt, JsonNode data) {

}
