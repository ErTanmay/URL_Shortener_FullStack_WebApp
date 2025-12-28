package com.tsl.urlshortner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tsl.urlshortner.dto.ForgetPasswordDto;
import com.tsl.urlshortner.dto.ForgotPasswordRequestDto;
import com.tsl.urlshortner.dto.LoginRequestDto;
import com.tsl.urlshortner.dto.LoginResponseDto;
import com.tsl.urlshortner.dto.UserDto;
import com.tsl.urlshortner.dto.VerificationDto;
import com.tsl.urlshortner.service.JWTUtil;
import com.tsl.urlshortner.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
	
	private final UserService userService;
	private final AuthenticationManager authManager;
	private final JWTUtil jwtUtil;
	
	public UserController(UserService userService, AuthenticationManager authManager, JWTUtil jwtUtil) {
		this.userService = userService;
		this.authManager = authManager;
		this.jwtUtil = jwtUtil;
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<UserDto> signUp(@RequestBody UserDto user) {
		log.info("User registration request : {} ",user);
		userService.registerUser(user);
		return ResponseEntity.ok(user);
	}
	
	
	@PostMapping("/verify")
	public ResponseEntity<String> verify(@RequestBody VerificationDto input){
		log.info("User verification request : {} ",input);
		if(userService.verify(input)) {
			return ResponseEntity.ok("Account verified successfully !");
		}else {
			return ResponseEntity.ok("Invalid otp, Kindly try again");
		}
	}


	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request){
		
		log.info("User login request : {} ",request);
		
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getUsername(), request.getPassword()));
		
		String token = jwtUtil.generateToken(request.getUsername());
		
		LoginResponseDto response = new LoginResponseDto();
		response.setToken(token);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/resend")
	public ResponseEntity<String> resend(@RequestBody ForgotPasswordRequestDto input){
		log.info("User resend OTP request : {} ",input);
		userService.resendVerificationCode(input.getEmail());
		return ResponseEntity.ok("Verification code has been sent to your mail id");
	}
	
	    @PutMapping("/update")
	    public ResponseEntity<String> updateUser(@RequestBody UserDto request) {
	    	log.info("User update request : {} ",request);
	        userService.updateUserDetails(request);
	        return ResponseEntity.ok("User details updated successfully");
	    }
	 
	    @PostMapping("/forget-password/send-code")
		public ResponseEntity<String> fpSendCode(@RequestBody ForgotPasswordRequestDto input){
	    	log.info("User forget password send OTP request : {} ",input);
			userService.sendVerificationCode(input.getEmail());
			return ResponseEntity.ok("Verification code has been sent to your mail id");
		}
	 
	    @PostMapping("/forget-password/verify")
	    public ResponseEntity<String> fpVerify(@RequestBody ForgetPasswordDto input){
	    	log.info("User forget password verify OTP request : {} ",input);
	    	VerificationDto verifyDto = new VerificationDto();
	    	verifyDto.setEmail(input.getEmail());
	    	verifyDto.setVerificationCode(input.getVerificationCode());
	    	if(userService.verify(verifyDto)) {
	    		userService.updateNewPassword(input);
	    	}
	    	return ResponseEntity.ok("Password updated successfully");
	    }
	 
}
