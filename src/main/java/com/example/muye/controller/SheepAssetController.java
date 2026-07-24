package com.example.muye.controller;

import com.example.muye.common.Result;
import com.example.muye.dto.ValuationRequest;
import com.example.muye.entity.SheepAsset;
import com.example.muye.service.SheepAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 羊只资产管理接口
 */
@RestController
@RequestMapping("/api/sheep")
@CrossOrigin
public class SheepAssetController {
    @Autowired
    private SheepAssetService sheepService;

    @PostMapping("/add")
    public Result<String> add(@RequestBody SheepAsset sheep) {
        sheepService.addNewSheep(sheep);
        return Result.success("添加成功");
    }

    @GetMapping("/{id}")
    public Result<SheepAsset> getOne(@PathVariable Long id) {
        return Result.success(sheepService.getSheepById(id));
    }

    @GetMapping("/list")
    public Result<List<SheepAsset>> list() {
        return Result.success(sheepService.getAllSheep());
    }

    @PutMapping("/update")
    public Result<String> update(@RequestBody SheepAsset sheep) {
        sheepService.updateSheep(sheep);
        return Result.success("更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        sheepService.deleteSheep(id);
        return Result.success("删除成功");
    }

    /**
     * 活体估值引擎（DBAVM 模型）：输入体重、健康评分，返回估值金额与风险等级
     */
    @PostMapping("/evaluate")
    public Result<Map<String, Object>> evaluate(@Valid @RequestBody ValuationRequest req) {
        return Result.success(sheepService.evaluateSheep(req));
    }

    /**
     * 资产确权流水线：MySQL 入库 + 区块链上链，返回多维度状态报告
     */
    @PostMapping("/confirm")
    public Result<Map<String, Object>> confirm(@RequestBody SheepAsset sheep) {
        return Result.success(sheepService.confirmSheep(sheep));
    }

    /**
     * 资产防伪核验：交叉比对 MySQL 物理数据与区块链数字指纹，输出信任状态
     */
    @GetMapping("/verify/{id}")
    public Result<Map<String, Object>> verify(@PathVariable Long id) {
        return Result.success(sheepService.verifySheep(id));
    }
}