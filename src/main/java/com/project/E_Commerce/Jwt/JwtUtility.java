package com.project.E_Commerce.Jwt;


import com.project.E_Commerce.UserDetails.CustomUserDetailsService;
import com.project.E_Commerce.UserDetails.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtility {

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

//token create
private String createToken(String email,String role,Integer id,String name)
{
    Map<String, Object> claims= new HashMap<>();
    claims.put("id",id);
    claims.put("role",role);
    claims.put("name",name);
    claims.put("email",email);

    return Jwts.builder()
            .setClaims(claims)
            .setSubject(email)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();

}
//generate token
public String generateToken(String email) {
    UserDetailsImpl userDetails= (UserDetailsImpl) customUserDetailsService.loadUserByUsername(email);
        Integer id = userDetails.getId();
        String  name= userDetails.getName();
     String role=   userDetails.getRole().name();
        return createToken(email, role, id, name);

    }

//Claims = the data inside the token given by the user
// Using the standard way JJWT parses a token

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

//Generic method to extract a specific field from claims

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);  // Apply a resolver function to pull one field
}
    //from the claims extract the header and the claims which  has the email  and the role and also the expiry

    public String   extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

}
