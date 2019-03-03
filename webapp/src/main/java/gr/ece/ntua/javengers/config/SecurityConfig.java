package gr.ece.ntua.javengers.config;


import gr.ece.ntua.javengers.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private UserSecurityService userSecurityService;

    private static final String SALT = "salt";

    @Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }

    private static final String[] PUBLIC_MATCHERS = {
            "/css/**",
            "/js/**",
            "/vendor/**",
            "/img/**",
            "/",
            "/home",
            "/index",
            "/about/**",
            "/contact/**",
            "/error/**",
            "/console/**",
            "/signup",
            "/product/list",
            "/entry/list/*"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().
                antMatchers(HttpMethod.GET, "/observatory/api/products").permitAll().
                antMatchers(HttpMethod.GET, "/observatory/api/prices").permitAll().
                antMatchers(HttpMethod.POST, "/observatory/api/products").permitAll().
                antMatchers(HttpMethod.POST, "/observatory/api/prices").permitAll().
                antMatchers(HttpMethod.POST, "/observatory/api/shops*").permitAll().
                antMatchers(HttpMethod.GET, "/observatory/api/products/*").permitAll().
                antMatchers(HttpMethod.PUT, "/observatory/api/products/*").permitAll().
                antMatchers(HttpMethod.PATCH, "/observatory/api/products/*").permitAll().
                antMatchers(HttpMethod.DELETE, "/observatory/api/products/*").permitAll().
                antMatchers(HttpMethod.PUT, "/observatory/api/shops/*").permitAll().
                antMatchers(HttpMethod.PATCH, "/observatory/api/shops/*").permitAll().
                antMatchers(HttpMethod.DELETE, "/observatory/api/shops/*").permitAll().
                antMatchers(HttpMethod.POST, "/observatory/api/login").permitAll().
                antMatchers(HttpMethod.POST, "/observatory/api/logout").permitAll();

        http
                .authorizeRequests().
                antMatchers(HttpMethod.GET, "/observatory/api/shops").permitAll().
                antMatchers(HttpMethod.GET, "/observatory/api/shops/*").permitAll();

        http
                .authorizeRequests().
    //            antMatchers(HttpMethod.POST, "login").permitAll().
                antMatchers(PUBLIC_MATCHERS).
                permitAll().anyRequest().authenticated().
                antMatchers("/profile/**").hasAuthority("user");
                // antMatchers(HttpMethod.POST, "/observatory/api/products").hasAuthority("user").
                // antMatchers(HttpMethod.POST, "/observatory/api/prices").hasAuthority("user").
                // antMatchers(HttpMethod.PUT, "/observatory/api/products/*").hasAuthority("user").
                // antMatchers(HttpMethod.DELETE, "/observatory/api/products/*").hasAuthority("user").
           //     antMatchers(HttpMethod.POST, "/observatory/api/shops*").hasAuthority("user").
            //    antMatchers(HttpMethod.PUT, "/observatory/api/shops/*").hasAuthority("user").
                // antMatchers(HttpMethod.DELETE, "/observatory/api/shops/*").hasAuthority("user");


        http
                .csrf().disable().cors().disable()
                .formLogin().failureUrl("/login?login=false").defaultSuccessUrl("/index").loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index?logout").deleteCookies("remember-me").permitAll()
                .and()
                .rememberMe();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//    	 auth.inMemoryAuthentication().withUser("user").password("password").roles("USER"); //This is in-memory authentication
        //auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
    }

}
