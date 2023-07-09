package com.victorkessler.quotationfreight.infrastructure.async.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.victorkessler.quotationfreight.domain.service.CalculateFreightService;
import com.victorkessler.quotationfreight.infrastructure.request.NewFreightRequest;
import com.victorkessler.quotationfreight.infrastructure.request.NewFreightRequestAvro;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
public class NewFreightConsumer {

    private CalculateFreightService service;

    public NewFreightConsumer(CalculateFreightService service) {
        this.service = service;
    }

    @KafkaListener(topics = "quotation-freight.new-freight", groupId = "freight-request", containerFactory = "kafkaListenerContainerFactory")
    public void calculateNewFreightRequest(Message<NewFreightRequestAvro> msg) throws JsonProcessingException{
        NewFreightRequestAvro payload = msg.getPayload();
        final var newFreightRequest = new NewFreightRequest(
                payload.getLatitude1(),
                payload.getLongitude1(),
                payload.getLatitude2(),
                payload.getLongitude2()
        );

        service.calculate(newFreightRequest);
    }

}
