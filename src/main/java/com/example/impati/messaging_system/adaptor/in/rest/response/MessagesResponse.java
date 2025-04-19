package com.example.impati.messaging_system.adaptor.in.rest.response;

import java.util.List;

public record MessagesResponse(
        List<MessageResponse> messages
) {

}
