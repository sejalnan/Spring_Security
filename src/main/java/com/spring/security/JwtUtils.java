package com.spring.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private String jwtSecret="YzVhZDM4ZmE5NzJhNGIxYzE0NzQ2ODk2ZmQ3ZjU3N2M4YmYxZTk0M2Q2" +
            "Zjg5Y2U0N2Q5M2UxYjM0ZjI2N2Y1Mg==";
    private int jwtExpirationMs=86400000;


    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken =request.getHeader("Authorization");
        if(bearerToken !=null && bearerToken.startsWith("Bearer "))
            return  bearerToken.substring(7);
        return null;
    }

    //Generate token and Issued It.

    public String generateTokenFromUsername(UserDetails userDetails) {
        return null;
    }

    public  boolean validateJwtToken(String jwtToken){
       try{
           Jwts.parser().verifyWith((SecretKey) key()).build().
                   parseSignedClaims(jwtToken);
       }catch(Exception e){
           e.printStackTrace();
       }
        return true;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String jwt) {
        return Jwts.parser().verifyWith((SecretKey) key())
                .build().parseSignedClaims(jwt)
                .getPayload().getSubject();
    }

    public Claims getAllClaims(String jwt) {
        return Jwts.parser().verifyWith((SecretKey) key())
                .build().parseSignedClaims(jwt)
                .getPayload();
    }
}
