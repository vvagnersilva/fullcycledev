package com.example.kafkamessagepipeline.infrastructure.web;

import com.example.kafkamessagepipeline.application.port.in.ListMessagesUseCase;
import com.example.kafkamessagepipeline.application.port.in.PublishMessageUseCase;
import com.example.kafkamessagepipeline.domain.Message;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    private final PublishMessageUseCase publishMessageUseCase;
    private final ListMessagesUseCase listMessagesUseCase;

    public MessageController(PublishMessageUseCase publishMessageUseCase, ListMessagesUseCase listMessagesUseCase) {
        this.publishMessageUseCase = publishMessageUseCase;
        this.listMessagesUseCase = listMessagesUseCase;
    }

    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public void publish(@RequestBody MessageRequest request) {
        publishMessageUseCase.publish(request.content());
    }

    @GetMapping("/messages")
    public List<Message> list() {
        return listMessagesUseCase.listAll();
    }
}
