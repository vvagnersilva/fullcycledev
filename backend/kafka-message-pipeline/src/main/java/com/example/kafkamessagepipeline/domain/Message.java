package com.example.kafkamessagepipeline.domain;

import java.time.Instant;

public record Message(Long id, String content, Instant receivedAt) {
}
