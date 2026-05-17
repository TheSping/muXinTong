package com.example.muye.controller;

import com.example.muye.dto.FinancePredictDTO;
import com.example.muye.dto.FinanceSubmitDTO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    // 基准利率 LPR (这里假设为 3.45%)
    private final double BASE_RATE = 3.45;

    /**
     * 接口 1：资格预审引擎 (计算建议额度和利率)
     */
    @PostMapping("/predict")
    public Map<String, Object> predictFinance(@RequestBody FinancePredictDTO req) {
        Map<String, Object> result = new HashMap<>();

        Double valuation = req.getAssetValuation();
        String risk = req.getRiskLevel();
        Integer period = req.getLoanPeriod();

        if (valuation == null || risk == null || period == null) {
            result.put("error", "估值、风险等级和贷款周期不能为空");
            return result;
        }

        // 1. 计算建议额度 (动态抵押率)
        double maxQuotaRatio;
        double riskPremium; // 风险溢价
        String auditAdvice;

        switch (risk) {
            case "低风险":
                maxQuotaRatio = 0.85;
                riskPremium = 0.0;
                auditAdvice = "建议通过";
                break;
            case "中风险":
                maxQuotaRatio = 0.75;
                riskPremium = 1.0; // 中风险加 1% 利息
                auditAdvice = "补充审核";
                break;
            default: // 高风险
                maxQuotaRatio = 0.60;
                riskPremium = 2.5; // 高风险加 2.5% 利息
                auditAdvice = "极高风险，建议拒贷";
        }

        double suggestedQuota = valuation * maxQuotaRatio;

        // 2. 计算建议利率 (基准利率 + 风险溢价 + 期限溢价)
        double termPremium = (period > 12) ? 0.5 : 0.0; // 超过1年加 0.5% 利息
        double suggestedRate = BASE_RATE + riskPremium + termPremium;

        // 3. 计算预计放款时间 (当前日期 + 平台1天 + 银行3天)
        LocalDate expectedDate = LocalDate.now().plusDays(4);

        // 组装返回给前端
        result.put("suggestedQuota", Math.round(suggestedQuota * 100.0) / 100.0);
        result.put("suggestedRate", Math.round(suggestedRate * 100.0) / 100.0);
        result.put("expectedDate", expectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        result.put("auditAdvice", auditAdvice);

        return result;
    }

    /**
     * 接口 2：正式提交融资申请
     */
    @PostMapping("/submit")
    public Map<String, Object> submitApplication(@RequestBody FinanceSubmitDTO req) {
        Map<String, Object> result = new HashMap<>();

        // TODO: 这里未来应该调用 Mapper 的 insert 方法把 req 存进 sys_finance 表
        // financeMapper.insert(financeEntity);

        // 模拟落盘成功
        result.put("code", 200);
        result.put("message", "融资申请提交成功！");
        result.put("status", "已提交融资申请资料"); // 对应你 UI 上的第一个节点

        System.out.println("✅ 收到融资申请：" + req.getApplicant() + "，金额：" + req.getApplyAmount());

        return result;
    }
}