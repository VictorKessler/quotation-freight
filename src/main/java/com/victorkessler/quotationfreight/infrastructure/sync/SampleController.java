package com.victorkessler.quotationfreight.infrastructure.sync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.victorkessler.quotationfreight.domain.model.Freight;
import com.victorkessler.quotationfreight.domain.service.CalculateFreightService;
import com.victorkessler.quotationfreight.infrastructure.request.NewFreightRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @Autowired
    private CalculateFreightService service;

    @PostMapping("/new-freight")
    public ResponseEntity<Freight> newFreight(@RequestBody NewFreightRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(service.calculate(request));
    }

}
