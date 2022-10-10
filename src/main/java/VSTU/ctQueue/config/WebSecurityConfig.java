package VSTU.ctQueue.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import VSTU.ctQueue.service.UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationEntryPoint entryPoint;

    @Value("${client.url}")
    private String clientUrl;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Setting Service to find User in the database.
        // And Setting PassswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(clientUrl).allowedMethods("GET", "POST");
    }

    @Override
    // @formatter:off
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        
        http
        .authorizeRequests()
        .antMatchers("/entrant", "/entrant/**","/index.html").permitAll()
        .and().authorizeRequests().antMatchers("/operator", "/operator/**", "/swagger-ui.html", "/auth/success").hasAnyRole("ADMIN","OPERATOR")
        .and().authorizeRequests().antMatchers("/reservation","/reservation/**").hasRole("ADMIN")
        .and().authorizeRequests().antMatchers("/registration", "/auth/login", "/auth/failure","/auth/logout").anonymous()
        .anyRequest().authenticated()
        .and()
        .httpBasic().authenticationEntryPoint(entryPoint);
    }
    // @formatter:on

}
