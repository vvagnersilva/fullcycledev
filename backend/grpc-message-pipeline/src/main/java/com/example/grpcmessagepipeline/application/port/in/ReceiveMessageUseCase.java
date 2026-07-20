package com.example.grpcmessagepipeline.application.port.in;

public interface ReceiveMessageUseCase {

    void receive(String content);
}
