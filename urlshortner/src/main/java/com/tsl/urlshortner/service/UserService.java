package com.tsl.urlshortner.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tsl.urlshortner.dto.ForgetPasswordDto;
import com.tsl.urlshortner.dto.UserDto;
import com.tsl.urlshortner.dto.VerificationDto;
import com.tsl.urlshortner.exception.EmailNotFoundException;
import com.tsl.urlshortner.exception.InvalidVerificationCodeException;
import com.tsl.urlshortner.exception.UserAlreadyVerifiedException;
import com.tsl.urlshortner.exception.UserNotFoundException;
import com.tsl.urlshortner.exception.VerificationCodeExpiredException;
import com.tsl.urlshortner.model.MyUser;
import com.tsl.urlshortner.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	private final PasswordEncoder encoder;
	private final UserRepository repository;
	private final EmailService emailService;
	
	public UserService(PasswordEncoder encoder, UserRepository repository, EmailService emailService) {
		this.encoder = encoder;
		this.repository = repository;
		this.emailService = emailService;
	}
	
	@Transactional
	public UserDto registerUser(UserDto user) {
		
		try {
		
		log.info("In user service userDTO : {}", user);
		MyUser newUser = new MyUser();
		newUser.setFName(user.getFName());
		newUser.setLName(user.getLName());
		newUser.setEmail(user.getEmail());
		newUser.setPhone(user.getPhone());
		newUser.setUsername(user.getUsername());
		newUser.setPassword(encoder.encode(user.getPassword()));
		newUser.setVerificationCode(generateVerificationCode());
		newUser.setVerificationExpiration(LocalDateTime.now().plusMinutes(15));
		newUser.setEnabled(false);
		sendVerificationCode(newUser);
		log.info("In user service newUser before save : {}", newUser);
		repository.save(newUser);
		
		}catch(Exception exception) {
			exception.printStackTrace();
		}
		return user;
	}

	@Transactional
	public boolean verify(VerificationDto input) {
		
		Optional<MyUser> newUser = repository.findByEmail(input.getEmail());
		
		if(newUser.isPresent()) {
			if(newUser.get().isEnabled()) {
				throw new UserAlreadyVerifiedException("User already verified");
				}
			MyUser user = newUser.get();
			if(user.getVerificationExpiration().isBefore(LocalDateTime.now())) {
				throw new VerificationCodeExpiredException("Verification code has expired");
			}
			if(input.getVerificationCode().equals(user.getVerificationCode())) {
				user.setEnabled(true);
				user.setVerificationCode(null);
				user.setVerificationExpiration(null);
				repository.save(user);
				return true;
			}else {
				throw new InvalidVerificationCodeException("Invalid verification code");
			}
		}else {
			throw new UserNotFoundException("User not found");
		}
	}
	
	@Transactional
	public void resendVerificationCode(String email) {
		Optional<MyUser> newUser = repository.findByEmail(email);
		
		if(newUser.isPresent()) {
			MyUser user = newUser.get();
			if(user.isEnabled()) {
				throw new UserAlreadyVerifiedException("User already verified");
				}
			user.setVerificationCode(generateVerificationCode());
			user.setVerificationExpiration(LocalDateTime.now().plusMinutes(15));
			sendVerificationCode(user);
			repository.save(user);
			
			}else {
				throw new UserNotFoundException("User not found");
			}
	}



	private void sendVerificationCode(MyUser user) {
		
	     String subject = "Account Verification";
		 String verificationCode = "verification code is : " + user.getVerificationCode();
		
		 String htmlMessage = "<html>"
	                + "<body style=\"font-family: Arial, sans-serif;\">"
	                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
	                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
	                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
	                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
	                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
	                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
	                + "</div>"
	                + "</div>"
	                + "</body>"
	                + "</html>";
		 
		 try {
			 emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
		 }catch(MessagingException e){
			 e.printStackTrace();
		 }
		
	}


	private String generateVerificationCode() {
		Random random = new Random();
		long code = random.nextLong(900000) + 100000;
		return String.valueOf(code);
	}


	@Transactional
	public void updateUserDetails(UserDto request) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName(); 
		
		Optional<MyUser> existingUser = repository.findByUsername(username);
		
		if(!existingUser.isPresent()) {
			throw new UserNotFoundException("User not found");
		}
		
	try {
		
		if((request.getFName() != null) && (request.getFName() != existingUser.get().getFName())) {
			existingUser.get().setFName(request.getFName());
		}
		
		if((request.getLName() != null) && (request.getLName() != existingUser.get().getLName())) {
			existingUser.get().setLName(request.getLName());
		}
		
		if((request.getEmail() != null) && (request.getEmail() != existingUser.get().getEmail())) {
			existingUser.get().setEmail(request.getEmail());
		}
		
		if((request.getPhone() != null) && (request.getPhone() != existingUser.get().getPhone())) {
			existingUser.get().setPhone(request.getPhone());
		}
		
		if((request.getPassword() != null) && (encoder.encode(request.getPassword()) != existingUser.get().getPassword())) {
			existingUser.get().setPassword(encoder.encode(request.getPassword()));
		}
		
		if((request.getUsername() != null) && (request.getUsername() != existingUser.get().getUsername())) {
			existingUser.get().setUsername(request.getUsername());
		}
		
		repository.save(existingUser.get());
		
	 }catch(Exception e) {
		 e.printStackTrace();
	 }
		
	}


	@Transactional
	public void updateNewPassword(ForgetPasswordDto input) {
		
		Optional<MyUser> newUser = repository.findByEmail(input.getEmail());
		if(!newUser.isPresent()) {
			throw new EmailNotFoundException("Email does not exist");
			}
		newUser.get().setPassword(encoder.encode(input.getPassword()));
		repository.save(newUser.get());
	}


	@Transactional
	public void sendVerificationCode(String email) {
		Optional<MyUser> newUser = repository.findByEmail(email);
		
		if(newUser.isPresent()) {
			
			MyUser user = newUser.get();
			user.setVerificationCode(generateVerificationCode());
			user.setVerificationExpiration(LocalDateTime.now().plusMinutes(15));
			user.setEnabled(false);
			sendVerificationCode(user);
			repository.save(user);
			
			}else {
				throw new UserNotFoundException("User not found");
			}
		
	}

}
 