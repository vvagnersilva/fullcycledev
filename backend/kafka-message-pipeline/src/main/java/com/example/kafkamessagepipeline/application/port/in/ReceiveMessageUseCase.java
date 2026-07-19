package com.example.kafkamessagepipeline.application.port.in;

public interface ReceiveMessageUseCase {

    void receive(String content);
}
