package com.tsl.urlshortner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tsl.urlshortner.model.MyUser;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Long>{
	
	public Optional<MyUser> findByUsername(String username);
	public Optional<MyUser> findByVerificationCode(String code);
	public Optional<MyUser> findByEmail(String email);

}
