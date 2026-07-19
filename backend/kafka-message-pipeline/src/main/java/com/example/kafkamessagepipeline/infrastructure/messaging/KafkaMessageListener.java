package com.example.kafkamessagepipeline.infrastructure.messaging;

import com.example.kafkamessagepipeline.application.port.in.ReceiveMessageUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class KafkaMessageListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);

    private final ReceiveMessageUseCase receiveMessageUseCase;

    public KafkaMessageListener(ReceiveMessageUseCase receiveMessageUseCase) {
        this.receiveMessageUseCase = receiveMessageUseCase;
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String content) {
        log.info("Mensagem recebida do Kafka: {}", content);
        receiveMessageUseCase.receive(content);
    }

    @KafkaListener(topics = "${app.kafka.topic}.DLT", groupId = "${spring.kafka.consumer.group-id}-dlt")
    public void consumeDeadLetter(ConsumerRecord<String, String> record) {
        String originalTopic = header(record, KafkaHeaders.DLT_ORIGINAL_TOPIC);
        String exceptionMessage = header(record, KafkaHeaders.DLT_EXCEPTION_MESSAGE);

        log.error("Mensagem morta recebida (topico original: {}, causa: {}): {}",
                originalTopic, exceptionMessage, record.value());

        // Ponto de extensao: reprocessar manualmente, salvar em tabela de erros
        // ou notificar um time responsavel.
    }

    private String header(ConsumerRecord<String, String> record, String headerName) {
        Header header = record.headers().lastHeader(headerName);
        return header != null ? new String(header.value(), StandardCharsets.UTF_8) : null;
    }
}
