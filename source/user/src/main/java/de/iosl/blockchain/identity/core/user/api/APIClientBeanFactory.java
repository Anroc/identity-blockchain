package de.iosl.blockchain.identity.core.user.api;

import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.core.shared.feign.FeignAdapterBeanFactory;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class APIClientBeanFactory {

    @Autowired
    private FeignAdapterBeanFactory feignAdapterBeanFactory;
    @Autowired
    private BlockchainIdentityConfig config;

    public APIClient createAPIClient(@NonNull String url) {
        return feignAdapterBeanFactory.buildBean(APIClient.class, url);
    }

    public String buildUrl(@NonNull String address, int port) {
        return feignAdapterBeanFactory.buildURL(config.getProtocol(), address, port);
    }
}
