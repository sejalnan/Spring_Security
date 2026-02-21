package com.spring.security.config;

import com.spring.security.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    DataSource datasource;

    @Autowired
    AuthTokenFilter authTokenFilter;

    //Enabling Basic Http authentication
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests->
                        authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN")
                                        .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/signin").permitAll()
                        .anyRequest().authenticated());
       // httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    //In Memory Authentication
    @Bean
     public UserDetailsService userDetailsService(){
        UserDetails user1= User.withUsername("user1")
              //  .password("{noop}pass@01")
                .password(passwordEncoder().encode("user@01"))
                .roles("USER") //ROLE_USER
                .build();

         UserDetails admin= User.withUsername("admin") //ROLE_ADMIN
                 .password(passwordEncoder().encode("admin@01"))
                 .roles("ADMIN")
                 .build();

         UserDetails user2= User.withUsername("user2")
                 .password(passwordEncoder().encode("user@02"))
                 .roles("USER")
                 .build();

        //  return  new InMemoryUserDetailsManager(user1,user2,admin);

        // Connecting with database
        JdbcUserDetailsManager userDetailsManager=
                new JdbcUserDetailsManager(datasource);

        if (!userDetailsManager.userExists((user1.getUsername()))) {
            userDetailsManager.createUser(user1);
        }
        if (!userDetailsManager.userExists((user2.getUsername()))) {
            userDetailsManager.createUser(user2);
        }
        if (!userDetailsManager.userExists((admin.getUsername()))) {
            userDetailsManager.createUser(admin);
        }

        return userDetailsManager;

     }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder){
        return builder.getAuthenticationManager();
    }

}
