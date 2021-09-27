package com.example.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.userservice.entity.Suser;
@Repository
public interface SuserRepo extends JpaRepository<Suser,Long>{
	Suser findByUsername(String username);

}
