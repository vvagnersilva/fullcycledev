package com.example.grpcmessagepipeline.application.port.in;

import com.example.grpcmessagepipeline.domain.Message;

import java.util.List;

public interface ListMessagesUseCase {

    List<Message> listAll();
}
