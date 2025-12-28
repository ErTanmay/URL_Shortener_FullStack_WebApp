package com.tsl.urlshortner.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tsl.urlshortner.service.JWTUtil;
import com.tsl.urlshortner.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter{
	
	private final JWTUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsServiceImpl;

	public JWTAuthenticationFilter(JWTUtil jwtUtil, UserDetailsServiceImpl userDetailsServiceImpl) {
		this.jwtUtil = jwtUtil;
		this.userDetailsServiceImpl = userDetailsServiceImpl;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
			FilterChain filterChain) throws ServletException, IOException {
		
		System.out.println("JWT FILTER HIT → " + request.getMethod() + " " + request.getServletPath());


	
		 String authHeader = request.getHeader("Authorization");
		 
		 if(authHeader != null && authHeader.startsWith("Bearer ")) {
	            String jwtToken = authHeader.substring(7);
	            String username = jwtUtil.extractUsername(jwtToken);
	            
	            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	            	 UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
	            	 
	            	 if(jwtUtil.isTokenValid(jwtToken)) {
	            		 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, 
	                                     null, userDetails.getAuthorities());
	            		 SecurityContextHolder.getContext().setAuthentication(authToken);
	            	 }
	            	 //filterChain.doFilter(request, response);
	            }
		 }
		 filterChain.doFilter(request, response);
	}
}
