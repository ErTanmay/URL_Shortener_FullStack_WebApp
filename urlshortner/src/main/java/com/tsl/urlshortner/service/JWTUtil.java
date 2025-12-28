package com.tsl.urlshortner.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JWTUtil {
	
		@Value("${spring.jwt.secret}")
		private String SECRET;
    
		@Value("${spring.jwt.expiration}")
		private long EXPIRATION_TIME;
		
		private Key key;
				
		 @PostConstruct
		    public void init() {
		        this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
		    }

	    
	    public String generateToken(String username) {
	        return Jwts.builder()
	                    .subject(username)
	                    .issuedAt(new Date())
	                    .expiration(new Date( System.currentTimeMillis() + EXPIRATION_TIME))
	                    .signWith(key)
	                    .compact();
	    }
	    
	    public String extractUsername(String token) {
	        return Jwts.parser()
	                .verifyWith((SecretKey) key)
	                .build()
	                .parseSignedClaims(token)
	                .getPayload()
	                .getSubject();
	        }
	    
	    public boolean isTokenValid(String token) {
	        try {
	            Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
	            return true;
	        }catch(JwtException e) {
	            return false;
	        }
	    }
}
