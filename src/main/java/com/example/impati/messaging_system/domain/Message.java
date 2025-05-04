package com.example.impati.messaging_system.domain;

import java.time.LocalDateTime;

public record Message(LocalDateTime createdAt, Object data) {

}
