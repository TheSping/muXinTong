package com.example.muye.service;

import org.fisco.bcos.sdk.client.Client;
import com.example.muye.contract.CowAssetChain;
import com.example.muye.entity.CowAsset;
import com.example.muye.mapper.CowAssetMapper;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CowAssetService {
    @Autowired
    private CowAssetMapper cowMapper;
    //************* 区块链 *******************
    // 注入我们刚才配置好的区块链客户端
    @Autowired
    private Client client;

    // ⚠️ 极其重要：这是你刚才在 Linux 里 deploy 生成的合约地址！
    private final String CONTRACT_ADDRESS = "0xf14e0d750650fc0a0062e0059dab772999122dd7";

    // 1. 添加一头牛，并做初始估值
    public String addNewCow(CowAsset cow) {
        // ==========================================
        // 第一重入账：存入传统 MySQL
        // ==========================================
        double initialValue = calculateValue(cow.getWeight());
        cow.setValuation(initialValue);
        cowMapper.insert(cow);
        System.out.println("✅ [MySQL] 数据已存入数据库，耳标号：" + cow.getEarTag());

        // ==========================================
        // 第二重入账：铸造数字身份并上链！
        // ==========================================
        try {
            // 1. 生成一把你的专属私钥（用来在区块链上签字画押）
            CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();

            // 2. 根据地址，找到区块链上的那份智能合约
            CowAssetChain cowContract = CowAssetChain.load(CONTRACT_ADDRESS, client, keyPair);

            // 3. 模拟生成一个视频/数据的数字指纹 (真实的应该用 SHA-256 算出来，这里为了演示先用时间戳拼一个)
            String fakeDataHash = "HASH_" + System.currentTimeMillis();

            // 4. 发送交易！调用合约里的 mintCowAsset 方法
            cowContract.mintCowAsset(cow.getEarTag(), fakeDataHash);

            System.out.println("🚀 [Blockchain] 上链成功！数字指纹：" + fakeDataHash);
            // 把哈希值 return 出去
            return fakeDataHash;

        } catch (Exception e) {
            System.err.println("❌ [Blockchain] 上链失败：" + e.getMessage());
            return "上链失败";
        }
    }

    // ==========================================
    // 新增：去区块链上查询资产指纹
    // ==========================================
    public String getCowHashFromChain(String earTag) {
        try {
            // 1. 准备钥匙和合约地址
            CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
            CowAssetChain cowContract = CowAssetChain.load(CONTRACT_ADDRESS, client, keyPair);

            // 2. 调用合约里的 getCowHash 方法
            String hash = cowContract.getCowHash(earTag);
            return hash;
        } catch (Exception e) {
            System.err.println("❌ 从区块链查询失败，原因：" + e.getMessage());
            return "未上链或查询失败";
        }
    }

    //*************************************

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