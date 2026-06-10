package service.config

import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest.toAnyEndpoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    fun actuatorSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher(toAnyEndpoint())
            cors { disable() }
            csrf { disable() }
            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }
            sessionManagement {
                sessionCreationPolicy = STATELESS
            }
        }
        return http.build()
    }

    @Bean
    @Order(LOWEST_PRECEDENCE)
    fun mcpSecurityFilterChain(http: HttpSecurity): SecurityFilterChain = http
        .cors { it.disable() }
        .csrf { it.disable() }
        .sessionManagement { it.sessionCreationPolicy(STATELESS) }
        .authorizeHttpRequests { auth ->
            auth
                .requestMatchers("/mcp/**").hasAnyAuthority("SCOPE_mcp:tools")
                .requestMatchers("/.well-known/**").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().denyAll()
        }
        .oauth2ResourceServer { builder ->
            builder
                .jwt {  }
                .protectedResourceMetadata {
                    it.protectedResourceMetadataCustomizer {
                        it.resource("http://localhost:8080/mcp")
                        it.authorizationServer("http://localhost:9000/realms/master")
                        it.scope("mcp:tools")
                        it.scope("offline_access")
                    }
                }
        }
        .build()
}
