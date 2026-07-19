package com.example.kafkamessagepipeline.application.port.out;

public interface MessagePublisherPort {

    void publish(String content);
}
