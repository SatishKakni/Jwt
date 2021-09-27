package com.example.userservice.service;

import java.util.List;


import com.example.userservice.entity.Srole;
import com.example.userservice.entity.Suser;

public interface SuserService {
	Suser saveUser(Suser user);
	Srole saveRole(Srole role);
	void addRoleToUser(String username,String rolename);
	Suser getUser(String username);
	List<Suser> getUsers();
	

}
