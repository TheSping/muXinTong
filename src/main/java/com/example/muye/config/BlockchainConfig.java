package com.example.muye.config;

import lombok.SneakyThrows;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.config.model.ConfigProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * FISCO BCOS 区块链客户端配置，从 AppConfig 读取节点与证书参数
 */
@Configuration
public class BlockchainConfig {

    @Autowired
    private AppConfig appConfig;

    @Bean
    @SneakyThrows
    public BcosSDK bcosSDK() {
        AppConfig.Blockchain bc = appConfig.getBlockchain();

        ConfigProperty configProperty = new ConfigProperty();

        Map<String, Object> network = new HashMap<>();
        network.put("peers", Arrays.asList(bc.getPeers().split(",")));
        configProperty.setNetwork(network);

        Map<String, Object> cryptoMaterial = new HashMap<>();
        cryptoMaterial.put("certPath", bc.getCertPath());
        cryptoMaterial.put("useSMCrypto", String.valueOf(bc.isUseSMCrypto()));
        configProperty.setCryptoMaterial(cryptoMaterial);

        ConfigOption configOption = new ConfigOption(configProperty);
        return new BcosSDK(configOption);
    }

    @Bean
    public Client client(BcosSDK bcosSDK) {
        return bcosSDK.getClient(appConfig.getBlockchain().getGroupId());
    }
}