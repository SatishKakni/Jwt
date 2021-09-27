package com.example.userservice.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.userservice.entity.Suser;
import com.example.userservice.service.SuserServiceImple;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.collections.MappingChange.Map;

import aj.org.objectweb.asm.Type;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private AuthenticationManager authenticationmanager;
	Logger log = LoggerFactory.getLogger(CustomAuthenticationFilter.class);
	
	public CustomAuthenticationFilter(AuthenticationManager authenticationmanager) {
		this.authenticationmanager =authenticationmanager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		//return super.attemptAuthentication(request, response);
		
		String username= request.getParameter("username");
		String password= request.getParameter("password");
		log.info("username :",username);
		log.info("password",password);

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		return authenticationmanager.authenticate(authenticationToken); 
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		//super.successfulAuthentication(request, response, chain, authResult);
		Suser user=(Suser) authentication.getPrincipal();
		Algorithm algorithm=Algorithm.HMAC256("secret".getBytes());
		String access_roken=JWT.create()
				.withSubject(user.getName())
				.withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
				.withIssuer(request.getRequestURI().toString())
				.sign(algorithm);
		String refresh_token=JWT.create()
				.withSubject(user.getName())
				.withExpiresAt(new Date(System.currentTimeMillis()+30*60*1000))
				.withIssuer(request.getRequestURI().toString())
				.sign(algorithm);
		/*response.setHeader("access_token", access_roken);
		response.setHeader("refresh_token", refresh_token);
*/	
		HashMap<String, String> tokens= new HashMap<String, String>();
		tokens.put("access_token", access_roken);
		tokens.put("refresh_token",refresh_token);
		
		new ObjectMapper().writeValue(response.getOutputStream(),tokens);
		
	}

}
