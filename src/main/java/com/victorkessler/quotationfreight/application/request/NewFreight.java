package com.victorkessler.quotationfreight.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NewFreight(
        @JsonProperty
        Integer distanceInMeters,
        @JsonProperty
        Integer pricePerMeters
) {}