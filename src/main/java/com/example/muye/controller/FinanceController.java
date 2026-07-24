package com.example.muye.controller;

import com.example.muye.common.Result;
import com.example.muye.dto.FinancePredictDTO;
import com.example.muye.dto.FinanceSubmitDTO;
import com.example.muye.entity.FinanceApplication;
import com.example.muye.service.FinanceApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资管理接口：资格预审、申请提交、查询、修改、删除
 */
@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private FinanceApplicationService financeService;

    /** 基准年利率 LPR */
    private final double BASE_RATE = 3.45;

    /**
     * 资格预审引擎：根据资产估值、风险等级、贷款周期计算建议额度与利率
     */
    @PostMapping("/predict")
    public Result<Map<String, Object>> predictFinance(@Valid @RequestBody FinancePredictDTO req) {
        Map<String, Object> data = new HashMap<>();

        double maxQuotaRatio;
        double riskPremium;
        String auditAdvice;

        switch (req.getRiskLevel()) {
            case "低风险":
                maxQuotaRatio = 0.85;
                riskPremium = 0.0;
                auditAdvice = "建议通过";
                break;
            case "中风险":
                maxQuotaRatio = 0.75;
                riskPremium = 1.0;
                auditAdvice = "补充审核";
                break;
            default:
                maxQuotaRatio = 0.60;
                riskPremium = 2.5;
                auditAdvice = "极高风险，建议拒贷";
        }

        double suggestedQuota = req.getAssetValuation() * maxQuotaRatio;
        double termPremium = (req.getLoanPeriod() > 12) ? 0.5 : 0.0;
        double suggestedRate = BASE_RATE + riskPremium + termPremium;
        LocalDate expectedDate = LocalDate.now().plusDays(4);

        data.put("suggestedQuota", Math.round(suggestedQuota * 100.0) / 100.0);
        data.put("suggestedRate", Math.round(suggestedRate * 100.0) / 100.0);
        data.put("expectedDate", expectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        data.put("auditAdvice", auditAdvice);

        return Result.success(data);
    }

    /**
     * 提交融资申请，落库存储
     */
    @PostMapping("/submit")
    public Result<FinanceApplication> submitApplication(@Valid @RequestBody FinanceSubmitDTO req) {
        FinanceApplication app = new FinanceApplication();
        app.setApplicant(req.getApplicant());
        app.setApplyAmount(req.getApplyAmount());
        app.setLoanPeriod(req.getLoanPeriod());
        app.setLoanUsage(req.getLoanUsage());
        app.setAssetValuation(req.getAssetValuation());
        app.setRiskLevel(req.getRiskLevel());
        app.setSuggestedQuota(req.getSuggestedQuota());
        app.setSuggestedRate(req.getSuggestedRate());
        app.setStatus("已提交融资申请资料");
        app.setCreateTime(LocalDateTime.now());

        financeService.insert(app);

        System.out.println("[OK] 融资申请已提交：" + req.getApplicant() + "，金额：" + req.getApplyAmount());

        return Result.success("融资申请提交成功", app);
    }

    @GetMapping("/list")
    public Result<List<FinanceApplication>> list() {
        return Result.success(financeService.getAll());
    }

    @GetMapping("/{id}")
    public Result<FinanceApplication> getOne(@PathVariable Long id) {
        return Result.success(financeService.getById(id));
    }

    @PutMapping("/update")
    public Result<String> update(@RequestBody FinanceApplication app) {
        financeService.update(app);
        return Result.success("更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        financeService.delete(id);
        return Result.success("删除成功");
    }
}