package com.example.grpcmessagepipeline.infrastructure.grpc;

import com.example.grpcmessagepipeline.application.port.in.ListMessagesUseCase;
import com.example.grpcmessagepipeline.application.port.in.PublishMessageUseCase;
import com.example.grpcmessagepipeline.grpc.ListMessagesRequest;
import com.example.grpcmessagepipeline.grpc.MessageReply;
import com.example.grpcmessagepipeline.grpc.MessageServiceGrpc;
import com.example.grpcmessagepipeline.grpc.PublishMessageRequest;
import com.example.grpcmessagepipeline.grpc.PublishMessageResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class MessageGrpcService extends MessageServiceGrpc.MessageServiceImplBase {

    private final PublishMessageUseCase publishMessageUseCase;
    private final ListMessagesUseCase listMessagesUseCase;

    public MessageGrpcService(PublishMessageUseCase publishMessageUseCase, ListMessagesUseCase listMessagesUseCase) {
        this.publishMessageUseCase = publishMessageUseCase;
        this.listMessagesUseCase = listMessagesUseCase;
    }

    @Override
    public void publishMessage(PublishMessageRequest request, StreamObserver<PublishMessageResponse> responseObserver) {
        publishMessageUseCase.publish(request.getContent());
        responseObserver.onNext(PublishMessageResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void listMessages(ListMessagesRequest request, StreamObserver<MessageReply> responseObserver) {
        listMessagesUseCase.listAll().forEach(message -> responseObserver.onNext(
                MessageReply.newBuilder()
                        .setId(message.id())
                        .setContent(message.content())
                        .setReceivedAt(message.receivedAt().toString())
                        .build()));
        responseObserver.onCompleted();
    }
}
