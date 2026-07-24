package com.example.muye.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 集中管理应用配置（区块链节点、合约地址等），与 application.properties 中的 app.* 前缀绑定
 */
@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {
    private Blockchain blockchain = new Blockchain();
    private Contract contract = new Contract();

    @Data
    public static class Blockchain {
        /** 区块链节点 IP 和端口，多个用逗号分隔 */
        private String peers = "127.0.0.1:20200";
        /** 群组 ID */
        private Integer groupId = 1;
        /** 证书存放目录（相对于 resources 的路径） */
        private String certPath = "conf";
        /** 是否使用国密加密 */
        private boolean useSMCrypto = false;
    }

    @Data
    public static class Contract {
        /** CowAssetChain 智能合约部署地址 */
        private String cowAssetAddress;
    }
}