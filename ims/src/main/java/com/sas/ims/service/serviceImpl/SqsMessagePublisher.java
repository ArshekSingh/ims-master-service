package com.sas.ims.service.serviceImpl;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SqsMessagePublisher {

    @Autowired
    private final AmazonSQSAsync amazonSqs;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public SqsMessagePublisher(final AmazonSQSAsync amazonSQSAsync) {
        this.amazonSqs = amazonSQSAsync;
    }

    public void send(final String messagePayload, final String queueAddress) {
        try {
            log.info("Building message for queue {}", queueAddress);
            SendMessageRequest sendMessageRequest = new SendMessageRequest().withMessageBody(messagePayload).withQueueUrl(queueAddress);
            amazonSqs.sendMessage(sendMessageRequest);
            log.info("Message sent to queue {}", queueAddress);
        } catch (Exception e) {
            log.error("Message not sent to queue {}", queueAddress + ". Reason: " + e.getMessage());
        }
    }
}
