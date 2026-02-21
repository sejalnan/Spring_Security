package com.spring.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private String jwtSecret="YzVhZDM4ZmE5NzJhNGIxYzE0NzQ2ODk2ZmQ3ZjU3N2M4YmYxZTk0M2Q2" +
            "Zjg5Y2U0N2Q5M2UxYjM0ZjI2N2Y1Mg==";
    private int jwtExpirationMs=86400000;


    public String getJwtFromHeader(){
        return  "";
    }

    //Generate token and Issued It.

    public String generateTokenFromUsername(String userName){

        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime()+ jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public  boolean validateJwtToken(){
        return true;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
