package com.kaua.monitoring.infrastructure.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.monitoring.infrastructure.services.gateways.MessengerGateway;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class SQSService implements MessengerGateway {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void sendMessage(Object message) throws JsonProcessingException {
        try (final var sqsClient = SqsClient.create()) {
            final var queueUrl = sqsClient.getQueueUrl(
                    GetQueueUrlRequest
                            .builder()
                            .queueName("MONITORING_QUEUE")
                            .build()).queueUrl();

            sqsClient.sendMessage(
                    SendMessageRequest
                            .builder()
                            .queueUrl(queueUrl)
                            .messageBody(this.mapper.writeValueAsString(message))
                            .build()
            );
        } catch (Exception ex) {
            throw ex;
        }
    }
}
