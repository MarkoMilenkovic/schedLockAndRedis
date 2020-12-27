package com.mile.mile.security.dao;

import com.mile.mile.security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<UserEntity, Integer> {
	
	UserEntity findByUsername(String username);
	
}