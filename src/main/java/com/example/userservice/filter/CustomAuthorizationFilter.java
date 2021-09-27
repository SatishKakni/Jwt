package com.example.userservice.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import antlr.Token;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(request.getServletPath().equals("/controller/login")) {
			filterChain.doFilter(request, response);
		}else {
			try {
				
			String authorizationHeader= null;
			String token =authorizationHeader.substring("Bearer".length());
			if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer"));
			Algorithm algorithm= Algorithm.HMAC256("secret".getBytes());
			JWTVerifier verifier=JWT.require(algorithm).build();
			DecodedJWT decodedJWT= verifier.verify(token);
			String username= decodedJWT.getSubject();
			String[] roles=decodedJWT.getClaim("roles").asArray(String.class);
			Collection<SimpleGrantedAuthority> authorities= new ArrayList<>();
			UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(username,null,authorities);
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			filterChain.doFilter(request, response);
			}catch(Exception e) {
				
			}
		}
		
	}

}
