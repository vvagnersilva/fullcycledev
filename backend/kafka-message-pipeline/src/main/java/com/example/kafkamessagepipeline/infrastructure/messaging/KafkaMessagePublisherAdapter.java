package com.example.kafkamessagepipeline.infrastructure.messaging;

import com.example.kafkamessagepipeline.application.port.out.MessagePublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessagePublisherAdapter implements MessagePublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessagePublisherAdapter.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String topic;

    public KafkaMessagePublisherAdapter(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(String content) {
        kafkaTemplate.send(topic, content).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Falha ao enviar mensagem para o topico {}: {}", topic, content, ex);
            } else {
                log.info("Mensagem enviada para o topico {} [partition={}, offset={}]",
                        topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}
