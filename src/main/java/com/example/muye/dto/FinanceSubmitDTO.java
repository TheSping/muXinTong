package com.example.muye.dto;

import lombok.Data;

@Data
public class FinanceSubmitDTO {
    private String applicant;      // 申请人
    private Double applyAmount;    // 申请金额
    private Integer loanPeriod;    // 贷款周期
    private String loanUsage;      // 贷款用途

    // 下面这些是系统预审算出来的，前端原样传回来一起存库
    private Double assetValuation;
    private String riskLevel;
    private Double suggestedQuota;
    private Double suggestedRate;
}