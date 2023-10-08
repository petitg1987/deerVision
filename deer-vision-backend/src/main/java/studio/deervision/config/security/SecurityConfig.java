package studio.deervision.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import studio.deervision.config.properties.ActuatorProperties;
import studio.deervision.config.properties.AdminProperties;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {

    private static final int PASSWORD_STRENGTH = 10;

    private final ActuatorProperties actuatorProperties;
    private final AdminProperties adminProperties;

    @Autowired
    public SecurityConfig(ActuatorProperties actuatorProperties, AdminProperties adminProperties){
        this.actuatorProperties = actuatorProperties;
        this.adminProperties = adminProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(PASSWORD_STRENGTH);
    }

    @Bean
    @Order(1)
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedMethods("GET", "POST", "DELETE")
                        .allowedOrigins(adminProperties.getAllowedOrigins().toArray(new String[0]));
            }
        };
    }

    @Bean
    @Order(2)
    public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(regexMatcher("^/actuator/.*"))
                .authorizeHttpRequests(authz -> authz.requestMatchers(regexMatcher("^/actuator/.*")).authenticated())
                .sessionManagement(a -> a.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    @Order(2)
    public InMemoryUserDetailsManager actuatorUserDetailsService() {
        UserDetails user = User.withUsername("actuator")
                .password(actuatorProperties.getPassword())
                .roles("ACTUATOR_USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    @Order(3)
    public SecurityFilterChain apiAdminLoginFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(regexMatcher("^/api/admin/login.*"))
                .authorizeHttpRequests(authz -> authz.requestMatchers(regexMatcher("^/api/admin/login.*")).permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain apiAdminFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(regexMatcher("^/api/admin/.*"))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(regexMatcher(HttpMethod.OPTIONS, "^/api/admin/.*")).permitAll() //allow CORS option calls
                        .requestMatchers(regexMatcher("^/api/admin/.*")).fullyAuthenticated())
                .addFilterAfter(new JWTAuthorizationFilter(adminProperties), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    @Order(5)
    public SecurityFilterChain apiWebsitePublicFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(regexMatcher("^/api/visitor/.*"))
                .authorizeHttpRequests(authz -> authz.requestMatchers(regexMatcher("^/api/.*")).permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    @Order(6)
    public SecurityFilterChain apiPublicFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(regexMatcher("^/api/.*"))
                .authorizeHttpRequests(authz -> authz.requestMatchers(regexMatcher("^/api/.*")).fullyAuthenticated())
                .addFilterBefore(new RequestKeyAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

}
