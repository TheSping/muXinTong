package com.example.muye.dto;

import lombok.Data;

@Data
public class ValuationRequest {
    private String earTag;
    private String breed;
    private Integer age;
    private Double weight;
    private Integer healthScore;
    private Double marketPrice;
}