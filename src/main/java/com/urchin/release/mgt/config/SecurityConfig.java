package com.urchin.release.mgt.config;

import com.urchin.release.mgt.config.properties.ActuatorProperties;
import com.urchin.release.mgt.config.properties.BinaryProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    public class ActuatorSecurity extends WebSecurityConfigurerAdapter {

        private ActuatorProperties actuatorProperties;

        @Autowired
        public ActuatorSecurity(ActuatorProperties actuatorProperties){
            this.actuatorProperties = actuatorProperties;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/actuator/**").authorizeRequests()
                    .anyRequest().authenticated()
                    .and().httpBasic()
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().csrf().disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("actuator")
                    .password(actuatorProperties.getPassword())
                    .roles("ACTUATOR_USER");
        }
    }

    @Configuration
    @Order(2)
    public class APIBinaryUploadSecurity extends WebSecurityConfigurerAdapter {

        private BinaryProperties binaryProperties;

        @Autowired
        public APIBinaryUploadSecurity(BinaryProperties binaryProperties){
            this.binaryProperties = binaryProperties;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception { //TODO not working well (return 302 in case of wrong auth)
            http.antMatcher("/api/binaries/**").authorizeRequests()
                    .antMatchers(HttpMethod.PUT).authenticated()
                    .and().httpBasic()
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().csrf().disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("api")
                    .password(binaryProperties.getUploadPassword())
                    .roles("API_UPLOAD_USER");
        }
    }

    @Configuration
    @Order(3)
    public class APIPublicSecurity extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**").authorizeRequests()
                    .anyRequest().permitAll()
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().csrf().disable();
        }
    }

    @Configuration
    @Order(4)
    public class WebAppSecurity extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().antMatchers("/js/**", "/css/**", "/img/**", "/logout").permitAll()
                    .anyRequest().authenticated()
                    .and().formLogin().loginPage("/login").permitAll()
                    .and().logout().permitAll();
        }

    }

}
