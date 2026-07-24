package com.example.muye.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 羊只资产实体，对应 sheep_asset 表
 */
@TableName("sheep_asset")
@Data
public class SheepAsset {
    private Long id;
    private String earTag;
    private Double weight;
    private Double valuation;
}