package com.d2j2.newsapi.security;



import com.d2j2.newsapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserRepository userRepository;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new SSUDS(userRepository);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http    .authorizeRequests()
                .antMatchers("/","/index","/error","/fragment/**","/css/**","/vendor/**","/js/**","/signup","/saveuser","/login").permitAll()
                .antMatchers("/usernewspage","/addcategories","/savegategories").access("hasAuthority('USER')")
                .antMatchers("/h2-console/**","/admin/**").access("hasAuthority('ADMIN')")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").successForwardUrl("/usernewspage").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

        http    .csrf().disable();

        http    .headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication().withUser("admin")
                .password(passwordEncoder().encode("password")).authorities("ADMIN")
                .and()
                .withUser("user")
                .password(passwordEncoder().encode("password")).authorities("USER")
                .and()
                .withUser("dom")
                .password(passwordEncoder().encode("password")).authorities("ADMIN")
                .and()
                .passwordEncoder(passwordEncoder());

        auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoder());

    }
}
