package hu.psprog.leaflet.tms.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Web Security configuration.
 *
 * @author Peter Smith
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String ENDPOINT_TRANSLATION_PACKS = "/translations\\?packs=.+$";
    private static final String ENDPOINT_TRANSLATIONS = "/translations/**";

    private static final String SCOPE_READ_TRANSLATIONS = "SCOPE_read:translations";
    private static final String SCOPE_WRITE_TRANSLATIONS = "SCOPE_write:translations";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests()
                    .regexMatchers(HttpMethod.GET, ENDPOINT_TRANSLATION_PACKS)
                        .permitAll()
                    .mvcMatchers(HttpMethod.GET, ENDPOINT_TRANSLATIONS)
                        .hasAuthority(SCOPE_READ_TRANSLATIONS)
                    .mvcMatchers(ENDPOINT_TRANSLATIONS)
                        .hasAuthority(SCOPE_WRITE_TRANSLATIONS)
                    .and()

                .csrf()
                    .disable()

                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()

                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)

                .build();
    }
}
