package hu.psprog.leaflet.tms.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

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
    private static final String ENDPOINT_ACTUATOR = "/actuator/**";

    private static final String SCOPE_READ_TRANSLATIONS = "SCOPE_read:translations";
    private static final String SCOPE_WRITE_TRANSLATIONS = "SCOPE_write:translations";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(regexMatcher(HttpMethod.GET, ENDPOINT_TRANSLATION_PACKS))
                            .permitAll()
                        .requestMatchers(HttpMethod.GET, ENDPOINT_ACTUATOR)
                            .permitAll()
                        .requestMatchers(HttpMethod.GET, ENDPOINT_TRANSLATIONS)
                            .hasAuthority(SCOPE_READ_TRANSLATIONS)
                        .requestMatchers(ENDPOINT_TRANSLATIONS)
                            .hasAuthority(SCOPE_WRITE_TRANSLATIONS))

                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwtConfigurer -> {}))

                .build();
    }
}
