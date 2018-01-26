package de.iosl.blockchain.identity.core.provider.config;

import de.iosl.blockchain.identity.core.shared.api.ProviderAPIConstances;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CredentialConfig credentialConfig;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication()
                .withUser(credentialConfig.getUsername())
                .password(credentialConfig.getPassword())
                .roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/users/*/register", "/users/ethID/*/claimIDs").permitAll()
                    .antMatchers(ProviderAPIConstances.API_PATH + "/**").permitAll()
                    .anyRequest().fullyAuthenticated()
                .and()
                    .httpBasic().realmName("blockchain-identity")
                .and()
                    .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .and()
                    .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**");
    }
}
