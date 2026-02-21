package com.spring.security.controller;

import com.spring.security.JwtUtils;
import com.spring.security.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    // @PreAuthorize("hasAnyRole('ADMIN','USER')")
   // @PreAuthorize("hasRole('ADMIN')") //METHOD-LEVEL-SECURITY
    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }

    @GetMapping("/user/hello")
    public String helloUser(){
        return "Hello, USER";
    }

    @GetMapping("/admin/hello")
    public String helloAdmin(){
        return "Hello, ADMIN";
    }

    @PostMapping("/signin")
    public String login(@RequestBody LoginRequest loginRequest){

        Authentication authentication;
        try{
            authentication=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

        }catch (AuthenticationException e){
            e.printStackTrace();
                    return "Could not authenticate";
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

       UserDetails userDetails= (UserDetails) authentication.getPrincipal();
       String jwtToken=jwtUtils.generateTokenFromUsername(userDetails);
       return jwtToken;

    }
}
