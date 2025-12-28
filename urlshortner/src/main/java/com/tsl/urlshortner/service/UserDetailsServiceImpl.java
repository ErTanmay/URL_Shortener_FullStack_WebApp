package com.tsl.urlshortner.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tsl.urlshortner.exception.UserNotEnabledException;
import com.tsl.urlshortner.model.MyUser;
import com.tsl.urlshortner.repository.UserRepository;

import lombok.Data;

@Service
@Data
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private final UserRepository userRepo;
	
	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		
		MyUser user = userRepo.findByUsername(username).get();
		
		if(user==null) {
			throw new UsernameNotFoundException("User does not exist");
		}else if(!user.isEnabled()) {
			throw new UserNotEnabledException("User not enabled as account is not verified yet");
		}
		
		User newUser = new User(user.getUsername(), user.getPassword(), Collections.emptyList());
		
		return newUser;
	}


	
}
