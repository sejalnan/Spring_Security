package com.spring.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PreAuthorize("hasRole('ADMIN')") //METHOD-LEVEL-SECURITY
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
}
