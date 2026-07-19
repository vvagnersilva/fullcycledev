package com.example.kafkamessagepipeline.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMessageRepository extends JpaRepository<MessageJpaEntity, Long> {
}
