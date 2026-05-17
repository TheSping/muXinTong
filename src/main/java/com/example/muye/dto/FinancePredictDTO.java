package com.example.muye.dto;

import lombok.Data;

@Data
public class FinancePredictDTO {
    private Double assetValuation; // 资产估值 (V_t)
    private String riskLevel;      // 风险等级 (低风险/中风险/高风险)
    private Integer loanPeriod;    // 贷款周期(月)
}