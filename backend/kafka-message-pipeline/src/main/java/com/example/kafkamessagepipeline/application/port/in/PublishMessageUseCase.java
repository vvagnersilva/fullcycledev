package com.example.kafkamessagepipeline.application.port.in;

public interface PublishMessageUseCase {

    void publish(String content);
}
