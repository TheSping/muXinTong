# 牧信通（MuXinTong）项目总结文档

> **项目描述**：牧信通后端服务，基于 Spring Boot 的活体畜牧资产管理与区块链存证平台，支持牛/羊资产管理、DBAVM 活体估值、FISCO BCOS 区块链确权、防伪核验及金融融资预审与申请。

---

## 一、技术栈

| 层次 | 技术 | 版本 |
|------|------|------|
| 框架 | Spring Boot | 2.7.18 |
| 语言 | Java | 11 |
| ORM | MyBatis-Plus | 3.5.3.1 |
| 数据库 | MySQL | 8.x |
| 区块链 | FISCO BCOS Java SDK | 2.10.0 |
| 构建工具 | Maven | 3.9+（自带 mvnw wrapper） |
| 校验 | spring-boot-starter-validation | — |
| 工具 | Lombok | — |

---

## 二、项目结构

```
muxintong/
├── pom.xml
├── src/main/java/com/example/muye/
│   ├── MuyeApplication.java              # 启动入口
│   ├── common/
│   │   ├── Result.java                    # 统一 REST 响应格式 {code, message, data}
│   │   └── GlobalExceptionHandler.java    # 全局异常拦截 (@RestControllerAdvice)
│   ├── config/
│   │   ├── AppConfig.java                 # 集中配置 (application.properties 绑定)
│   │   └── BlockchainConfig.java          # FISCO BCOS 客户端配置
│   ├── contract/
│   │   └── CowAssetChain.java             # 智能合约 Java Wrapper (预编译 ABI/二进制)
│   ├── controller/
│   │   ├── CowAssetController.java        # 牛只资产接口 /api/cow
│   │   ├── SheepAssetController.java      # 羊只资产接口 /api/sheep
│   │   ├── SysUserController.java         # 用户认证接口 /api/user
│   │   └── FinanceController.java         # 融资管理接口 /api/finance
│   ├── dto/
│   │   ├── FinancePredictDTO.java         # 融资预审请求
│   │   ├── FinanceSubmitDTO.java          # 融资提交请求
│   │   ├── UserAuthDTO.java               # 注册/登录请求
│   │   └── ValuationRequest.java          # 活体估值请求
│   ├── entity/
│   │   ├── CowAsset.java                  # cow_asset 表实体
│   │   ├── SheepAsset.java                # sheep_asset 表实体
│   │   ├── SysUser.java                   # sys_user 表实体
│   │   └── FinanceApplication.java        # sys_finance 表实体
│   ├── exception/
│   │   ├── BusinessException.java         # 业务异常基类
│   │   ├── AuthException.java             # 认证异常 → 401
│   │   ├── AssetNotFoundException.java    # 资产不存在 → 404
│   │   └── BlockchainException.java       # 区块链异常 → 502
│   ├── mapper/
│   │   ├── CowAssetMapper.java            # MyBatis-Plus BaseMapper
│   │   ├── SheepAssetMapper.java          # MyBatis-Plus BaseMapper
│   │   ├── SysUserMapper.java             # MyBatis-Plus BaseMapper
│   │   └── FinanceApplicationMapper.java  # MyBatis-Plus BaseMapper
│   └── service/
│       ├── CowAssetService.java           # 牛只核心业务 (CRUD + 估值 + 上链 + 核验)
│       ├── SheepAssetService.java         # 羊只核心业务 (CRUD + 估值 + 上链 + 核验)
│       ├── SysUserService.java            # 用户认证业务
│       └── FinanceApplicationService.java # 融资申请业务
├── src/main/resources/
│   ├── application.properties             # 数据库、区块链、合约配置
│   └── conf/                              # FISCO BCOS SDK 证书
│       ├── ca.crt
│       ├── sdk.crt
│       └── sdk.key
└── src/test/
    └── java/com/example/muye/
        └── MuyeApplicationTests.java      # 上下文加载测试
```

---

## 三、数据库设计

### 3.1 `cow_asset`（牛只资产表）

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键（MyBatis-Plus 雪花 ID） |
| `ear_tag` | VARCHAR(100) | 耳标号（业务唯一标识，也是链上 key） |
| `weight` | DOUBLE | 体重（kg） |
| `valuation` | DOUBLE | 估值金额（DBAVM 公式：weight × 1.5） |

### 3.2 `sheep_asset`（羊只资产表）

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键（MyBatis-Plus 雪花 ID） |
| `ear_tag` | VARCHAR(100) | 耳标号 |
| `weight` | DOUBLE | 体重（kg） |
| `valuation` | DOUBLE | 估值金额 |

### 3.3 `sys_user`（系统用户表）

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键（自增） |
| `username` | VARCHAR(50) | 用户名（唯一） |
| `password` | VARCHAR(100) | MD5 + 盐值加密 |
| `phone` | VARCHAR(20) | 手机号 |
| `role` | VARCHAR(20) | 角色（默认 USER） |
| `create_time` | DATETIME | 创建时间 |

### 3.4 `sys_finance`（融资申请表）

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键（自增） |
| `applicant` | VARCHAR(100) | 申请人 |
| `apply_amount` | DOUBLE | 申请金额 |
| `loan_period` | INT | 贷款周期（月） |
| `loan_usage` | VARCHAR(500) | 贷款用途 |
| `asset_valuation` | DOUBLE | 关联资产估值 |
| `risk_level` | VARCHAR(20) | 风险等级 |
| `suggested_quota` | DOUBLE | 建议额度 |
| `suggested_rate` | DOUBLE | 建议利率 |
| `status` | VARCHAR(50) | 申请状态 |
| `create_time` | DATETIME | 创建时间 |

### 3.5 区块链链上数据结构

智能合约中以 `earTag` 为 key 存储 Asset 结构体：

| 字段 | 说明 |
|------|------|
| `earTag` | 耳标号（索引 key） |
| `dataHash` | SHA-256 数字指纹（0x + 64 位十六进制） |
| `timestamp` | 上链时间戳 |
| `owner` | 铸造者地址 |

---

## 四、已实现功能

### 4.1 用户认证

| 功能 | 接口 | 方法 | 说明 |
|------|------|------|------|
| 注册 | `/api/user/register` | POST | 校验用户名唯一，MD5+盐加密入库 |
| 登录 | `/api/user/login` | POST | 校验用户名密码，返回 UUID Token |

### 4.2 牛只资产管理

| 功能 | 接口 | 方法 | 说明 |
|------|------|------|------|
| 新增 | `/api/cow/add` | POST | 存入 MySQL + 计算估值 + 上链存证 |
| 列表 | `/api/cow/list` | GET | 查询全部 |
| 单条 | `/api/cow/{id}` | GET | 按 ID 查询 |
| 修改 | `/api/cow/update` | PUT | 体重变更自动重算估值 |
| 删除 | `/api/cow/delete/{id}` | DELETE | 按 ID 删除 |
| 估算 | `/api/cow/evaluate` | POST | DBAVM 活体估值（体重 × 1.5）+ 健康评分风险分级 |
| 确权 | `/api/cow/confirm` | POST | MySQL + 区块链双写，返回四维状态报告 |
| 核验 | `/api/cow/verify/{id}` | GET | MySQL 物理数据 vs 区块链数字指纹交叉比对 |

### 4.3 羊只资产管理

接口与牛只完全对称，路径前缀 `/api/sheep`，功能与牛一致。

### 4.4 DBAVM 估值引擎

- **估值公式**（统一）：`valuation = weight × 1.5`
- **风险分级**：≥90 低风险、80-89 中风险、<80 高风险（仅影响文案措辞）
- **输入**：`weight`（必填）、`healthScore`（0-100，必填）、`marketPrice`（可选）
- **返回**：估值金额、风险等级、DBAVM 模型名称、评估时间、文案报告

### 4.5 金融融资管理

| 功能 | 接口 | 方法 | 说明 |
|------|------|------|------|
| 资格预审 | `/api/finance/predict` | POST | 动态抵押率（60%-85%）+ LPR 基准 3.45% + 风险溢价 |
| 提交申请 | `/api/finance/submit` | POST | 完整落库 sys_finance 表 |
| 查询全部 | `/api/finance/list` | GET | 列表 |
| 查询单条 | `/api/finance/{id}` | GET | 按 ID |
| 修改 | `/api/finance/update` | PUT | 更新状态等字段 |
| 删除 | `/api/finance/delete/{id}` | DELETE | 删除申请 |

---

## 五、架构特性

### 5.1 统一返回格式

所有接口返回 `Result<T>`：

```json
{ "code": 200, "message": "操作成功", "data": { ... } }
```

### 5.2 异常体系

| 异常类 | HTTP 状态 | 触发场景 |
|--------|----------|----------|
| `AuthException` | 401 | 用户名已存在、用户名不存在、密码错误 |
| `AssetNotFoundException` | 404 | 数据库查无此牛/羊 |
| `BlockchainException` | 502 | 区块链上链或查询失败 |
| `BusinessException` | 400 | 其他业务异常 |
| `MethodArgumentNotValidException` | 400 | `@Valid` 字段校验失败 |
| `Exception` | 500 | 未知异常兜底 |

### 5.3 参数校验

所有 DTO 使用 `javax.validation` 注解（`@NotNull`、`@NotBlank`、`@Min`、`@Max`），Controller 参数加 `@Valid`，由全局异常处理器统一转换错误信息。

### 5.4 区块链特性

- **真实哈希**：使用 `SHA-256` 对 `earTag + weight + valuation + timestamp` 生成 64 位十六进制指纹
- **密钥复用**：`@PostConstruct` 初始化一次 `CryptoKeyPair`，全生命周期复用
- **配置外置**：节点 IP、群组 ID、证书路径、合约地址全部由 `application.properties` 管理
- **异常透明**：`@SneakyThrows` 替代 try-catch，异常经 `BlockchainException` 统一由 Handler 处理

---

## 六、配置项速查

`application.properties` 中所有可配置项：

```properties
# 数据库
spring.datasource.url=...
spring.datasource.username=...
spring.datasource.password=...

# 区块链
app.blockchain.peers=127.0.0.1:20200    # 节点地址，多个逗号分隔
app.blockchain.group-id=1               # 群组 ID
app.blockchain.cert-path=conf           # 证书路径 (resources 下)
app.blockchain.use-sm-crypto=false      # 是否国密

# 合约
app.contract.cow-asset-address=0x...    # CowAssetChain 部署地址
```

---

> 文档生成时间：2026-07-24
