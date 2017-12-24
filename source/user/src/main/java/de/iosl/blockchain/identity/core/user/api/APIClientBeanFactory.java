package de.iosl.blockchain.identity.core.user.api;

import de.iosl.blockchain.identity.core.shared.feign.FeignAdapterBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class APIClientBeanFactory extends FeignAdapterBeanFactory {

    @Bean
    public APIClient createAPIClient() {
        return createAdapter(APIClient.class);
    }
}
