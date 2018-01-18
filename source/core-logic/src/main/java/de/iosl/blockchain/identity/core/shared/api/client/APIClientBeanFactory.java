package de.iosl.blockchain.identity.core.shared.api.client;

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

    public <T> T createAPIClient(@NonNull String url, @NonNull Class<T> clazz) {
        return feignAdapterBeanFactory.buildBean(clazz, url);
    }
}
