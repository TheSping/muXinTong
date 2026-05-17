以下是后端目前可用的核心 API 接口文档总结

### 1. 活体估值引擎接口 (DBAVM 模型)

- **接口地址**：`POST /api/cow/evaluate`
- **功能**：接收基础数据，计算最终估值、风险等级并生成报告文案。
- **前端请求 (Body - JSON)**：
    - `weight` (Double): 体重
    - `healthScore` (Integer): 健康评分 (0-100)
    - `marketPrice` (Double): 市场参考价
- **后端返回示例**：JSON
    
    ```
    `{
      "valuationAmount": 21896.0,
      "riskLevel": "低风险",
      "valuationModel": "DBAVM 估值模型",
      "evaluateTime": "2026-04-16 18:07",
      "reportText": "当前活体估值结果为 ¥ 21896.0，综合体重...建议纳入可融资资产池。"
    }`
    

### 2. 资产确权流水线接口 (上链存证)

- **接口地址**：`POST /api/cow/confirm`
- **功能**：前端点击提交确权后，后端自动落盘 MySQL 并部署智能合约上链，返回全套 UI 状态。
- **前端请求 (Body - JSON)**：包含牛的基本信息，如 `{"earTag": "A-009", "weight": 750.0}`
- **后端返回示例 (直接对应前端状态框)**：JSON
  
    ```
    `{
      "blockchainHash": "HASH_1776334297308",
      "chainStatus": "已上链",
      "chainTime": "2026-04-16 18:11:37",
      "aiCheckResult": "一致",
      "iotDataIntegrity": "96%",
      "statusIot": "已采集 (设备在线)",
      "statusAi": "通过 (识别一致)",
      "statusAsset": "有效 (可确权)",
      "statusRisk": "低风险 (正常范围)",
      "financeQualification": "建议进入估值流程"
    }`
    

### 3. 资产防伪核验接口

- **接口地址**：`GET /api/cow/verify/{id}`
- **功能**：根据牛的数据库 ID，交叉比对 MySQL 物理数据与区块链数字指纹，输出信任状态。
- **后端返回示例**：JSON

    ```
    `{
      "assetId": 2043712921490006017,
      "blockchainFingerprint": "HASH_1776332524337",
      "trustStatus": "✅ 高度可信：该资产已通过区块链存证",
      "physicalData": { "earTag": "A-008", "weight": 800.0 }
    }`
    

### 4. 基础列表与操作接口

- **查询全部资产**：`GET /api/cow/list` (返回牛资产数组)
- **删除特定资产**：`DELETE /api/cow/delete/{id}`
- **修改资产信息**：`PUT /api/cow/update`
