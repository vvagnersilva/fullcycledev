package com.example.grpcmessagepipeline.application.port.in;

public interface PublishMessageUseCase {

    void publish(String content);
}
