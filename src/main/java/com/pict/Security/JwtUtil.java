package com.pict.Security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JwtUtil {

    // Reading values from : application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    // Secret key ko "Key" object main convert karnege
    private SecretKey getSignInKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails userDetails){
        // this starts building the JWT token , setts it issued date, expiration time, signs it with secret key(i.e makes the SIGNATURE(JWT = HEADER.PAYLOAD.SIGNATURE)) and the compacts it
        return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)).signWith(getSignInKey()).compact();
    }

    public String extractUsername(String token){
        // helps to extract the username that was used in the JWT PAYLOAD
        return Jwts.parser().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token){

        try{
            Jwts.parser().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
            return true;
        }
        catch(JwtException e){
            return false;
        }
    }

}
