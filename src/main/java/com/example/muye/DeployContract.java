package com.example.muye;

import com.example.muye.contract.CowAssetChain;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.config.model.ConfigProperty;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import java.util.*;

/**
 * 一次性部署脚本：将 CowAssetChain 合约部署到链上并打印地址。
 * 用法：在服务器上运行 java -cp ... com.example.muye.DeployContract
 */
public class DeployContract {
    public static void main(String[] args) throws Exception {
        System.out.println("===== 部署 CowAssetChain 合约 =====");

        ConfigProperty cp = new ConfigProperty();
        Map<String, Object> net = new HashMap<>();
        net.put("peers", Arrays.asList("127.0.0.1:20200"));
        cp.setNetwork(net);
        Map<String, Object> cm = new HashMap<>();
        cm.put("certPath", "conf");
        cp.setCryptoMaterial(cm);
        BcosSDK sdk = new BcosSDK(new ConfigOption(cp));
        Client client = sdk.getClient(1);

        CryptoKeyPair kp = client.getCryptoSuite().createKeyPair();
        CowAssetChain contract = CowAssetChain.deploy(client, kp);

        System.out.println("===== 部署成功 =====");
        System.out.println("合约地址: " + contract.getContractAddress());
        System.out.println("将上面这行填到 application.properties:");
        System.out.println("app.contract.cow-asset-address=" + contract.getContractAddress());
    }
}