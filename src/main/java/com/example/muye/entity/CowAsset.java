package com.example.muye.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("cow_asset")
@Data
public class CowAsset {
    private Long id;
    private String earTag;
    private Double weight;
    private Double valuation;

}