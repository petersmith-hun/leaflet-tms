package hu.psprog.leaflet.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * Spring Boot entry point.
 *
 * @author Peter Smith
 */
@SpringBootApplication
public class TranslationManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TranslationManagementServiceApplication.class, args);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer applicationConfigPropertySource() {

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("version.properties"));

        return configurer;
    }
}
