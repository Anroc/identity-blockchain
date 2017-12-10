package de.iosl.blockchain.identity.core.shared.ds;

import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.core.shared.ds.beats.HeartBeatAdapter;
import de.iosl.blockchain.identity.core.shared.ds.registry.DiscoveryClientAdapter;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

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
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(clazz, url);
    }
}
