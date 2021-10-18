package studio.deervision.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import studio.deervision.config.properties.ActuatorProperties;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final int PASSWORD_STRENGTH = 10;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(PASSWORD_STRENGTH);
    }

    @Configuration
    @Order(1)
    public static class ActuatorSecurity extends WebSecurityConfigurerAdapter {

        private final ActuatorProperties actuatorProperties;

        @Autowired
        public ActuatorSecurity(ActuatorProperties actuatorProperties){
            this.actuatorProperties = actuatorProperties;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.regexMatcher("^/actuator/.*").authorizeRequests()
                    .regexMatchers("^/actuator/.*").authenticated()
                    .and().httpBasic()
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().csrf().disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser("actuator").password(actuatorProperties.getPassword()).roles("ACTUATOR_USER");
        }
    }

    @Configuration
    @Order(2)
    public static class APIAdminLoginSecurity extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .regexMatchers("^/api/admin/login.*").permitAll()
                    .and().csrf().disable();
        }
    }

    @Configuration
    @Order(3)
    public static class APIAdminSecurity extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.regexMatcher("^/api/admin/.*")
                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .regexMatchers("^/api/admin/.*")
                    .fullyAuthenticated()
                    .and().csrf().disable();
        }
    }

    @Configuration
    @Order(4)
    public static class APIPublicSecurity extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.regexMatcher("^/api/.*")
                    .addFilterBefore(new UserKeyAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .regexMatchers("^/api/.*")
                    .fullyAuthenticated()
                    .and().csrf().disable();
        }
    }

}
