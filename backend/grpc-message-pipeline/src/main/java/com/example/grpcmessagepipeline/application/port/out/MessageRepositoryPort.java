package com.example.grpcmessagepipeline.application.port.out;

import com.example.grpcmessagepipeline.domain.Message;

import java.util.List;

public interface MessageRepositoryPort {

    Message save(Message message);

    List<Message> findAll();
}
