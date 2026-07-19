package com.example.kafkamessagepipeline.application.port.out;

import com.example.kafkamessagepipeline.domain.Message;

import java.util.List;

public interface MessageRepositoryPort {

    Message save(Message message);

    List<Message> findAll();
}
