package com.example.muye.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 融资申请提交请求参数
 */
@Data
public class FinanceSubmitDTO {
    @NotBlank(message = "申请人不能为空")
    private String applicant;

    @NotNull(message = "申请金额不能为空")
    @Min(value = 0, message = "申请金额不能小于0")
    private Double applyAmount;

    @NotNull(message = "贷款周期不能为空")
    @Min(value = 1, message = "贷款周期必须大于0")
    private Integer loanPeriod;

    private String loanUsage;

    private Double assetValuation;
    private String riskLevel;
    private Double suggestedQuota;
    private Double suggestedRate;
}