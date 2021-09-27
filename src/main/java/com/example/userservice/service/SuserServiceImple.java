package com.example.userservice.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.catalina.User;
import org.apache.commons.logging.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.entity.Srole;
import com.example.userservice.entity.Suser;
import com.example.userservice.repo.SroleRepo;
import com.example.userservice.repo.SuserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SuserServiceImple implements SuserService,UserDetailsService {
	@Autowired
	private  SuserRepo userRepo;
	@Autowired
	private  SroleRepo roleRepo;
	Logger log =LoggerFactory.getLogger(SuserServiceImple.class);
	/*@Autowired
	private PasswordEncoder PasswordEncoder;
*/
	@Override
	public Suser saveUser(Suser user) {
		log.info("Saving new User to the database",user.getName());
		//user.setPassword(PasswordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}

	@Override
	public Srole saveRole(Srole role) {
		log.info("Saving new Role to the database",role.getName());
		return roleRepo.save(role);
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
		log.info("Adding role to user ",username,rolename);
		Suser user= userRepo.findByUsername(username);
		Srole role= roleRepo.findByName(rolename);
		user.getRoles().add(role);
	}

	@Override
	public Suser getUser(String username) {
		log.info("Fetching user",username);
		return userRepo.findByUsername(username);
		
	}

	@Override
	public List<Suser> getUsers() {
		log.info("Fetching all users ");
		return userRepo.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Suser user= userRepo.findByUsername(username);
		if(user==null) {
			log.error("user not found in the database");
			throw new UsernameNotFoundException("user not found in the database");
		}else {
			log.info("user not found in the database",username);
		}
		Collection<SimpleGrantedAuthority> authorities= new ArrayList<SimpleGrantedAuthority>();
		user.getRoles().forEach(role ->{
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
	}

}
