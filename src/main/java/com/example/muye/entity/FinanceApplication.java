package com.example.muye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 融资申请实体，对应 sys_finance 表
 */
@Data
@TableName("sys_finance")
public class FinanceApplication {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String applicant;
    private Double applyAmount;
    private Integer loanPeriod;
    private String loanUsage;
    private Double assetValuation;
    private String riskLevel;
    private Double suggestedQuota;
    private Double suggestedRate;
    private String status;
    private LocalDateTime createTime;
}