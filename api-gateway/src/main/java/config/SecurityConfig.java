package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


// Because this project is based on spring web flux and not spring cloud MVC
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        // Since we are communication only through postman client
        serverHttpSecurity.csrf()
                .disable()
                // To disable authentication for this url since it is used only for static content of eureka server
                .authorizeExchange(exchange -> exchange.pathMatchers("/eureka/**")
                        .permitAll()
                        // But all other exchanges are to be authenticated
                        .anyExchange()
                        .authenticated())
                // Enable resource server capabilities & JWT capability
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        // creating adn returning object of type security filter chain
        return serverHttpSecurity.build();
    }
}
