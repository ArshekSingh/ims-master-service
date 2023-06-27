package com.sas.ims.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sas.ims.dto.RequestOrderDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.ImsRequestOrder;
import com.sas.ims.service.RequestOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@AllArgsConstructor
public class ImsRequestOrderListener {
    private final ObjectMapper objectMapper;
    private final RequestOrderService requestOrderService;

    @SqsListener(value = "${aws.sqs.requirementUpdate.url}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveMessage(String message) throws JsonMappingException, JsonProcessingException, ObjectNotFoundException, BadRequestException {
        log.info("Message Received using SQS Listener ,thread {}", Thread.currentThread().getName());
        var imsRequestOrder = objectMapper.readValue(message,
                RequestOrderDto.class);

        if (Objects.nonNull(imsRequestOrder)) {
            log.info("Event received to sync policy data to vendor for xsell loan ID: {}",
                    imsRequestOrder.getXSellId());
            var requestOrderResponse = requestOrderService.addRequestOrder(imsRequestOrder);
        }
    }

}
