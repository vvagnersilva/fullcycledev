package com.example.grpcmessagepipeline.application;

import com.example.grpcmessagepipeline.application.port.in.ListMessagesUseCase;
import com.example.grpcmessagepipeline.application.port.in.PublishMessageUseCase;
import com.example.grpcmessagepipeline.application.port.in.ReceiveMessageUseCase;
import com.example.grpcmessagepipeline.application.port.out.MessageRepositoryPort;
import com.example.grpcmessagepipeline.domain.Message;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MessageService implements PublishMessageUseCase, ReceiveMessageUseCase, ListMessagesUseCase {

    private final MessageRepositoryPort messageRepositoryPort;

    public MessageService(MessageRepositoryPort messageRepositoryPort) {
        this.messageRepositoryPort = messageRepositoryPort;
    }

    @Override
    public void publish(String content) {
        receive(content);
    }

    @Override
    public void receive(String content) {
        messageRepositoryPort.save(new Message(null, content, Instant.now()));
    }

    @Override
    public List<Message> listAll() {
        return messageRepositoryPort.findAll();
    }
}
