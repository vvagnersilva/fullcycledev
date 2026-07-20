package com.example.grpcmessagepipeline.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "message_entity")
public class MessageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Instant receivedAt;

    public MessageJpaEntity() {
    }

    public MessageJpaEntity(String content, Instant receivedAt) {
        this.content = content;
        this.receivedAt = receivedAt;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }
}
