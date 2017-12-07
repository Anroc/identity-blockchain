package de.iosl.blockchain.identity.core.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@ComponentScan(basePackages = "de.iosl.blockchain.identity.core")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
