package com.project.E_Commerce.Controller.Authentication;

import com.project.E_Commerce.Jwt.JwtUtility;
import com.project.E_Commerce.UserDetails.CustomUserDetailsService;
import com.project.E_Commerce.dto.Authentication.AuthLoginRequest;
import com.project.E_Commerce.dto.Authentication.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

 @Autowired
    private AuthenticationManager authenticationManager;

 @Autowired
    private CustomUserDetailsService customUserDetailsService;

 @Autowired
    private JwtUtility jwtUtility;


 @PostMapping("/")
    public ResponseEntity<?> login(@RequestBody AuthLoginRequest authLoginRequest)
 {

     authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authLoginRequest.getEmail(),authLoginRequest.getPassword()));

     UserDetails userDetails = customUserDetailsService.loadUserByUsername(authLoginRequest.getEmail());
     String token = jwtUtility.generateToken(userDetails.getUsername());
     System.out.println(" The generated token is  " + token);
     return ResponseEntity.ok(new AuthResponse(token));


 }

}
