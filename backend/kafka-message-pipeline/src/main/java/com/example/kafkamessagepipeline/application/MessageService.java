package com.example.kafkamessagepipeline.application;

import com.example.kafkamessagepipeline.application.port.in.ListMessagesUseCase;
import com.example.kafkamessagepipeline.application.port.in.PublishMessageUseCase;
import com.example.kafkamessagepipeline.application.port.in.ReceiveMessageUseCase;
import com.example.kafkamessagepipeline.application.port.out.MessagePublisherPort;
import com.example.kafkamessagepipeline.application.port.out.MessageRepositoryPort;
import com.example.kafkamessagepipeline.domain.Message;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MessageService implements PublishMessageUseCase, ReceiveMessageUseCase, ListMessagesUseCase {

    private final MessagePublisherPort messagePublisherPort;
    private final MessageRepositoryPort messageRepositoryPort;

    public MessageService(MessagePublisherPort messagePublisherPort, MessageRepositoryPort messageRepositoryPort) {
        this.messagePublisherPort = messagePublisherPort;
        this.messageRepositoryPort = messageRepositoryPort;
    }

    @Override
    public void publish(String content) {
        messagePublisherPort.publish(content);
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
