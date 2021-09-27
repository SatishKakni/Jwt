package com.example.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.userservice.entity.Srole;
@Repository
public interface SroleRepo extends JpaRepository<Srole,Long> {
	Srole findByName(String name);

}
