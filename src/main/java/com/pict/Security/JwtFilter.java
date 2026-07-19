package com.pict.Security;

import com.pict.config.SecurityConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// it extends the OncePerRequestFilter so that the filer gets applied during each request
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService){
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // this reads the token into authHeader that start as follows :
        // Authorization : Bearer eyJhbGciOiJIUzI1Ni...(<-Token)
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // only storing the token part
        String jwtToken = authHeader.substring(7);
        // extracting email from token
        String username = jwtUtil.extractUsername(jwtToken);

        // checking weather the email isn't null and also the user is not authenticated already
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if(jwtUtil.validateToken(jwtToken)){
                // this tells that the user is authenticated
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null , userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
//                SecurityContextHolder.setAuthentication() → "Woh chit Spring ke register mein chipkao"
                // security context holder acts as a register in Spring boot that
            }
        }
        filterChain.doFilter(request, response);
    }
}
