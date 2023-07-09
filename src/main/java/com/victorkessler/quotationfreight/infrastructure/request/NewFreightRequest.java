package com.victorkessler.quotationfreight.infrastructure.request;

public record NewFreightRequest(
        double latitude1,
        double longitude1,
        double latitude2,
        double longitude2){}
