package de.iosl.blockchain.identity.core.shared.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperBeanFactory {

    @Autowired
    private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    /**
     * Provides a by the spring boot context configured object mapper.
     * See the <a href="https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#howto-customize-the-jackson-objectmapper">
     *     object mapper spring boot
     *     </a>
     *     documentation for more information
     *
     * @return a object mapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        jackson2ObjectMapperBuilder.configure(objectMapper);
        return objectMapper;
    }
}
