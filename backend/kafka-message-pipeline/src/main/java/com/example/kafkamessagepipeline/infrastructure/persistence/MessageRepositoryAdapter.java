package com.example.kafkamessagepipeline.infrastructure.persistence;

import com.example.kafkamessagepipeline.application.port.out.MessageRepositoryPort;
import com.example.kafkamessagepipeline.domain.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageRepositoryAdapter implements MessageRepositoryPort {

    private final SpringDataMessageRepository springDataMessageRepository;

    public MessageRepositoryAdapter(SpringDataMessageRepository springDataMessageRepository) {
        this.springDataMessageRepository = springDataMessageRepository;
    }

    @Override
    public Message save(Message message) {
        MessageJpaEntity saved = springDataMessageRepository.save(
                new MessageJpaEntity(message.content(), message.receivedAt()));
        return toDomain(saved);
    }

    @Override
    public List<Message> findAll() {
        return springDataMessageRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private Message toDomain(MessageJpaEntity entity) {
        return new Message(entity.getId(), entity.getContent(), entity.getReceivedAt());
    }
}
