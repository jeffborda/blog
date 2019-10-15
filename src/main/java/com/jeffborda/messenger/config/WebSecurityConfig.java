package com.jeffborda.messenger.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Bean /*Add this to Salt & Hash passwords with BCrypt */
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .csrf().disable() /*cross site request forgery (may not work w/ loc host */
                    .cors().disable() /*cross origin resource sharing */
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/", "/login", "/signup","/login/username-taken", "/*.css").permitAll() /* users allowed w/o logging in */
                    .antMatchers(HttpMethod.POST, "/signup").permitAll() /* GRANT ACCESS!! */
                    .anyRequest().authenticated() /* Can be opened up with '.permitAll()' */
                .and()
                    .formLogin()
                    .loginPage("/login") /* specifies we've made our own login page @/login */
                    .defaultSuccessUrl("/my-profile") /* where they are sent with successful login */
//                    .failureUrl("/login") /* failed login redirect */
                .and()
                    .logout()
                    .logoutUrl("/logout") /* route to logout users - no controller required */
                    .logoutSuccessUrl("/"); /* where to redirect the user after logout */
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
