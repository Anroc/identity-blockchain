package de.iosl.blockchain.identity.core.shared.ds;

import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatAdapter;
import de.iosl.blockchain.identity.core.shared.ds.registry.DiscoveryClientAdapter;
import de.iosl.blockchain.identity.lib.exception.InterServiceCallError;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignAdapterFactory {

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

    private <T> T createAdapter(Class<T> clazz) {
        String url = String.format("%s://%s:%d",
                blockchainIdentityConfig.getProtocol(),
                blockchainIdentityConfig.getDiscoveryService().getAddress(),
                blockchainIdentityConfig.getDiscoveryService().getPort());

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
