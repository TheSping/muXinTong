package com.example.muye.service;

import com.example.muye.entity.CowAsset;
import com.example.muye.mapper.CowAssetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CowAssetService {
    @Autowired
    private CowAssetMapper cowMapper;

    // 1. 添加一头牛，并做初始估值
    public void addNewCow(CowAsset cow) {
        double initialValue = calculateValue(cow.getWeight());
        cow.setValuation(initialValue);
        cowMapper.insert(cow);
    }
    // 2. 查 - 根据ID查询单头牛
    public CowAsset getCowById(Long id) {
        return cowMapper.selectById(id);
    }

    // 3. 查 - 查询所有牛的列表
    public List<CowAsset> getAllCows() {
        return cowMapper.selectList(null); // 传 null 表示没有筛选条件，查全部
    }

    // 4. 改 - 修改牛的信息
    public void updateCow(CowAsset cow) {
        // 如果修改了体重，顺便重新算一下估值
        if (cow.getWeight() != null) {
            cow.setValuation(calculateValue(cow.getWeight()));
        }
        cowMapper.updateById(cow); // 根据对象里的 id 自动去改其他字段
    }

    // 5. 删 - 根据ID删除
    public void deleteCow(Long id) {
        cowMapper.deleteById(id);
    }

    // DBAVM 2.0 估值算法
    private double calculateValue(Double weight) {
        return weight != null ? weight * 1.5 : 0; // 简单演示逻辑：体重1.5倍
    }
}