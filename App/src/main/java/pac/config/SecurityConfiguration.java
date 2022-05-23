package pac.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pac.security.handler.AuthenticationHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.ALWAYS;
import static pac.security.Authorities.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationHandler authenticationHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                //.authorizeRequests()
                .sessionManagement().sessionCreationPolicy(ALWAYS)
                .and()
                .authorizeRequests()
                .antMatchers("/", "/registration", "/endGame").permitAll()
                .antMatchers("/page1/**").hasAnyAuthority(ROLE_PLAYER1, ROLE_ADMIN)
                .antMatchers("/page2/**").hasAnyAuthority(ROLE_PLAYER2, ROLE_ADMIN)
                .antMatchers("/page3/**").hasAnyAuthority(ROLE_PLAYEROTHER, ROLE_ADMIN)
                .antMatchers("/pageGhost/**").hasAnyAuthority(ROLE_GHOST, ROLE_ADMIN)
                .antMatchers("/clear").hasAuthority(ROLE_ADMIN)
                .and()
                .formLogin().loginPage("/").permitAll().loginProcessingUrl("/perform-login")
                .usernameParameter("login")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .logout()
                .addLogoutHandler(authenticationHandler)
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/img/**", "/messages.properties");

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}