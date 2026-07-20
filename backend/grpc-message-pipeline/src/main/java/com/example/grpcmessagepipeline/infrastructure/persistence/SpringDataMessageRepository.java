package com.example.grpcmessagepipeline.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMessageRepository extends JpaRepository<MessageJpaEntity, Long> {
}
