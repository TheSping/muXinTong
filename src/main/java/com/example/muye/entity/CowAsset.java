package com.example.muye.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("cow_asset")
public class CowAsset {
    private Long id;
    private String earTag;
    private Double weight;
    private Double valuation;

    // ================= 把下面这段全部粘贴进去 =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEarTag() {
        return earTag;
    }

    public void setEarTag(String earTag) {
        this.earTag = earTag;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getValuation() {
        return valuation;
    }

    public void setValuation(Double valuation) {
        this.valuation = valuation;
    }
}