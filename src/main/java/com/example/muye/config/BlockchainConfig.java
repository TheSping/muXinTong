package com.example.muye.config;

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.config.model.ConfigProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class BlockchainConfig {

    @Bean
    public BcosSDK bcosSDK() throws ConfigException {
        ConfigProperty configProperty = new ConfigProperty();

        // 1. 配置网络：告诉 Java 咱们的区块链节点 IP 和端口是多少
        Map<String, Object> network = new HashMap<>();
        network.put("peers", Arrays.asList("127.0.0.1:20200")); // 你的节点0的默认端口
        configProperty.setNetwork(network);

        // 2. 配置证书：告诉 Java 刚才放进 resources 里的那 3 把钥匙在哪
        Map<String, Object> cryptoMaterial = new HashMap<>();
        cryptoMaterial.put("certPath", "conf");
        cryptoMaterial.put("useSMCrypto", "false"); // 我们用的是国际标准加密，不是国密
        configProperty.setCryptoMaterial(cryptoMaterial);

        // 3. 启动引擎
        ConfigOption configOption = new ConfigOption(configProperty);
        return new BcosSDK(configOption);
    }

    @Bean
    public Client client(BcosSDK bcosSDK) {
        // 返回操作群组 1 的客户端
        return bcosSDK.getClient(1);
    }
}