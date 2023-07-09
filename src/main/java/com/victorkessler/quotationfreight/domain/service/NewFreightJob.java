package com.victorkessler.quotationfreight.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.victorkessler.quotationfreight.infrastructure.request.NewFreightRequest;
import com.victorkessler.quotationfreight.infrastructure.request.NewFreightRequestAvro;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class NewFreightJob {

    KafkaTemplate kafkaTemplate;

    public NewFreightJob(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 60000)
    public void newFreightCreator() throws JsonProcessingException {
        final var request = getNewFreightRequest();

        sendMessage(request);
    }

    private NewFreightRequest getNewFreightRequest() {

        final var originCoordinates = getRandomCoordinates();
        final var latitude1 = originCoordinates[0];
        final var longitude1 = originCoordinates[1];

        final var destinyCoordinates = getRandomCoordinates();
        final var latitude2 = destinyCoordinates[0];
        final var longitude2 = destinyCoordinates[1];


        return new NewFreightRequest(latitude1, longitude1, latitude2, longitude2);
    }

    public void sendMessage(NewFreightRequest msg) throws JsonProcessingException {
        final var newFreightRequestAvro = NewFreightRequestAvro.newBuilder()
                .setLatitude1(msg.latitude1())
                .setLongitude1(msg.longitude1())
                .setLatitude2(msg.latitude2())
                .setLongitude2(msg.longitude2())
                .build();

        kafkaTemplate.send("quotation-freight.new-freight", UUID.randomUUID().toString(), newFreightRequestAvro);
    }

    public double[] getRandomCoordinates() {
        Random random = new Random();
        double latitude = -90 + 180 * random.nextDouble();
        double longitude = -180 + 360 * random.nextDouble();
        return new double[]{latitude, longitude};
    }

}
