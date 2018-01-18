package de.iosl.blockchain.identity.core.shared.api.client;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class APIClientRegistry<T> {

    @Autowired
    private APIClientBeanFactory apiClientBeanFactory;

    private final Map<String, T> apiClients;
    private final Class<T> clazz;

    public APIClientRegistry(Class<T> clazz) {
        this.apiClients = new HashMap<>();
        this.clazz = clazz;
    }

    public T register(@NonNull String url) {
        T apiClient = apiClientBeanFactory.createAPIClient(url, clazz);
        apiClients.put(url, apiClient);
        return apiClient;
    }

    public Optional<T> get(@NonNull String url) {
        return Optional.ofNullable(apiClients.get(url));
    }

    public T getOrRegister(@NonNull String url) {
        return get(url).orElseGet(() -> register(url));
    }
}
