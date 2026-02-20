package com.spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    //Enabling Basic Http authentication

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        httpSecurity.
                authorizeHttpRequests(authorizeRequests->
                        authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN")
                                        .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                        .anyRequest().authenticated());
        httpSecurity.httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }

    //In Memory Authentication
    @Bean
     public UserDetailsService userDetailsService(){
        UserDetails user1= User.withUsername("user1")
                .password("{noop}pass@01")
                .roles("USER") //ROLE_USER
                .build();

         UserDetails admin= User.withUsername("admin") //ROLE_ADMIN
                 .password("{noop}admin@01").roles("ADMIN")
                 .build();

         UserDetails user2= User.withUsername("user2")
                 .password("{noop}pass@02")
                 .roles("USER")
                 .build();

        return  new InMemoryUserDetailsManager(user1,user2,admin);
     }

}
