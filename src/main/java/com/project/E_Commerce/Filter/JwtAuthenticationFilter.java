package com.project.E_Commerce.Filter;

import com.project.E_Commerce.Jwt.JwtUtility;
import com.project.E_Commerce.UserDetails.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.naming.factory.SendMailFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

      final String authHeader=  request.getHeader("Authorization");
      final  String token;
      final String header;

      if (authHeader==null||!authHeader.startsWith("Bearer "))
      {
          filterChain.doFilter(request,response);
          return;
      }
      token= authHeader.substring(7);
        try {
            header = jwtUtility.extractUsername(token);
        }catch (io.jsonwebtoken.ExpiredJwtException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"JWT token is expired\"}");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Invalid JWT token\"}");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }
        if(header!=null  && SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(header);
            String emailFromDatabase = userDetails.getUsername();

            if (jwtUtility.validateToken(token,emailFromDatabase))
            {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());//user authentication in the spring securtiy
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            else {
                System.out.println("JWT is invalid or expired");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String errorJson = "{\"error\": \"JWT token is expired or invalid\"}";
                response.getWriter().write(errorJson);
                response.getWriter().flush();
                response.getWriter().close();
                return;
            }
        }
        filterChain.doFilter(request,response);

    }
}
