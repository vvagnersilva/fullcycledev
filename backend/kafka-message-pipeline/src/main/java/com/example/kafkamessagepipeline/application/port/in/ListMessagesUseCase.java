package com.example.kafkamessagepipeline.application.port.in;

import com.example.kafkamessagepipeline.domain.Message;

import java.util.List;

public interface ListMessagesUseCase {

    List<Message> listAll();
}
