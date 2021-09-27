package com.example.userservice.api;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.userservice.entity.Srole;
import com.example.userservice.entity.Suser;
import com.example.userservice.service.SuserService;
import com.fasterxml.jackson.databind.ObjectMapper;
@RestController
@RequestMapping("/controller")
public class SuserResource {
	@Autowired
	private  SuserService userservice;
	
	@GetMapping("/users")
	public ResponseEntity<List<Suser>> getUsers(){
		return ResponseEntity.ok().body(userservice.getUsers());
	}
	
	@PostMapping("/user/save")
	public ResponseEntity<Suser> saveUser(@RequestBody Suser user){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("controller/user/save").toUriString());
		return ResponseEntity.created(uri).body(userservice.saveUser(user));
	}
	@PostMapping("/role/save")
	public ResponseEntity<Srole> saveRole(@RequestBody Srole role){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("controller/role/save").toUriString());
		return ResponseEntity.created(uri).body(userservice.saveRole(role));
	}
	@PostMapping("/role/addtouser")
	public ResponseEntity<?>addRoleToUser(@RequestBody RoleToUserForm form){
		userservice.addRoleToUser(form.getUsername(),form.getRolename());
		return ResponseEntity.ok().build();
	}
	@GetMapping("/role/addtouser")
	public void refreshToken(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) {
			
			try {
				
				String authorizationHeader= null;
				String refresh_token =authorizationHeader.substring("Bearer".length());
				if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer"));
				Algorithm algorithm= Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier=JWT.require(algorithm).build();
				DecodedJWT decodedJWT= verifier.verify(refresh_token);
				String username= decodedJWT.getSubject();
				Suser user = userservice.getUser(username);
				String access_roken=JWT.create()
						.withSubject(user.getName())
						.withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
						.withIssuer(request.getRequestURI().toString())
						.sign(algorithm);
			
				HashMap<String, String> tokens= new HashMap<String, String>();
				tokens.put("access_token", access_roken);
				tokens.put("refresh_token",refresh_token);
				
				new ObjectMapper().writeValue(response.getOutputStream(),tokens);
				}catch(Exception e) {
					
				}

	}
	class RoleToUserForm{
		private String username;
		private String rolename;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getRolename() {
			return rolename;
		}
		public void setRolename(String rolename) {
			this.rolename = rolename;
		}
	}

}
