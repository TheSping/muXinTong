package com.example.muye.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 融资资格预审请求参数
 */
@Data
public class FinancePredictDTO {
    @NotNull(message = "资产估值不能为空")
    private Double assetValuation;

    @NotBlank(message = "风险等级不能为空")
    private String riskLevel;

    @NotNull(message = "贷款周期不能为空")
    @Min(value = 1, message = "贷款周期必须大于0")
    private Integer loanPeriod;
}