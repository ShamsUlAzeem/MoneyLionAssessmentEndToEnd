package shams.moneylionassessment.assessment.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shams.moneylionassessment.assessment.view.Login;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends VaadinWebSecurity {

    @Value( "${settings.user.admin-username:shams}" )
    private String adminUsername;

    @Value("${settings.user.admin-password:password}")
    private String adminPassword;

    @Value("${settings.user.admin-role:ADMIN}")
    private String adminRole;

    final DataSource dataSource;
    final PasswordEncoder passwordEncoder;
    final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(DataSource dataSource, PasswordEncoder passwordEncoder, JwtTokenFilter jwtTokenFilter) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                .antMatchers("/api/user**").authenticated()
                .antMatchers("/api/feature**").authenticated()
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(jdbcUserDetailsManager());
        super.configure(http);
        setLoginView(http, Login.class, "/logout");
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
            "/api/auth/login",
            "/api/user**",
            "/api/feature**"
        );
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager()
    {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(dataSource);

        // Admin user seeding
        if(!jdbcUserDetailsManager.userExists(adminUsername)) {
            jdbcUserDetailsManager.createUser(User.withUsername(adminUsername).password(passwordEncoder.encode(adminPassword)).roles(adminRole).build());
        }

        return jdbcUserDetailsManager;
    }

}