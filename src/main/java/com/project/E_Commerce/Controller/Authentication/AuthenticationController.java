package com.project.E_Commerce.Controller.Authentication;

import com.project.E_Commerce.Entity.User.User;
import com.project.E_Commerce.Jwt.JwtUtility;
import com.project.E_Commerce.Repository.User.UserRepo;
import com.project.E_Commerce.UserDetails.CustomUserDetailsService;
import com.project.E_Commerce.dto.Authentication.AuthLoginRequest;
import com.project.E_Commerce.dto.Authentication.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

 @Autowired
 private UserRepo userRepo;

 @PostMapping("/")
    public ResponseEntity<?> login(@RequestBody AuthLoginRequest authLoginRequest)
 {

     authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authLoginRequest.getEmail(),authLoginRequest.getPassword()));

     UserDetails userDetails = customUserDetailsService.loadUserByUsername(authLoginRequest.getEmail());
     String token = jwtUtility.generateToken(userDetails.getUsername());
     User user = userRepo.findByEmail(authLoginRequest.getEmail())
             .orElseThrow(() -> new UsernameNotFoundException("User not found"));

     AuthResponse response = new AuthResponse(
             token,
             user.getId(),
             user.getName(),
             user.getRole().name(),
             user.getEmail(),
             user.getStatus().name(),
             user.getPhoneNumber()
     );
     return ResponseEntity.ok(response);
 }

}
