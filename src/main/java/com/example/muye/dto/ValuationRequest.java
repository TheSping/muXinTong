package com.example.muye.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 活体估值请求参数
 */
@Data
public class ValuationRequest {
    private String earTag;
    private String breed;
    private Integer age;

    @NotNull(message = "体重不能为空")
    private Double weight;

    @NotNull(message = "健康评分不能为空")
    @Min(value = 0, message = "健康评分不能小于0")
    @Max(value = 100, message = "健康评分不能大于100")
    private Integer healthScore;

    private Double marketPrice;
}