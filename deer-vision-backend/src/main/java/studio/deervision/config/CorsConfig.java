package studio.deervision.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import studio.deervision.config.properties.AdminProperties;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    private final AdminProperties adminProperties;

    public CorsConfig(AdminProperties adminProperties) {
        this.adminProperties = adminProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "DELETE")
                .allowedOrigins(adminProperties.getAllowedOrigins().toArray(new String[0]));
    }
}