package de.iosl.blockchain.identity.core.shared.config;

import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;

public class CorsPermitAllConfiguration extends CorsConfiguration {

    @Override
    public CorsPermitAllConfiguration applyPermitDefaultValues() {
        super.applyPermitDefaultValues();
        addAllowedMethod(HttpMethod.DELETE);
        addAllowedMethod(HttpMethod.PUT);
        return this;
    }
}
