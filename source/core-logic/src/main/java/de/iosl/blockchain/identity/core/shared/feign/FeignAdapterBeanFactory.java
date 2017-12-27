package de.iosl.blockchain.identity.core.shared.feign;

import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatAdapter;
import de.iosl.blockchain.identity.core.shared.ds.registry.DiscoveryClientAdapter;
import de.iosl.blockchain.identity.lib.exception.InterServiceCallError;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignAdapterBeanFactory {

    @Autowired
    private BlockchainIdentityConfig blockchainIdentityConfig;

    @Bean
    public DiscoveryClientAdapter discoveryClientAdapter() {
        return createAdapter(DiscoveryClientAdapter.class);
    }

    @Bean
    public HeartBeatAdapter heartBeatAdapter() {
        return createAdapter(HeartBeatAdapter.class);
    }

    public  <T> T createAdapter(Class<T> clazz) {
        String url = buildURL(
                blockchainIdentityConfig.getProtocol(),
                blockchainIdentityConfig.getDiscoveryService().getAddress(),
                blockchainIdentityConfig.getDiscoveryService().getPort());
        return buildBean(clazz, url);
    }

    public String buildURL(@NonNull String protocol, @NonNull String address, int port) {
        return String.format("%s://%s:%d", protocol, address, port);
    }

    public <T> T buildBean(@NonNull Class<T> clazz, @NonNull String url) {
        return Feign.builder()
                .errorDecoder((methodKey, response) ->
                        new InterServiceCallError(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "InterService call error [" + methodKey +
                                        ", " + response.status() + "]: " + response.reason(),
                                response.body()))
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(clazz, url);
    }
}