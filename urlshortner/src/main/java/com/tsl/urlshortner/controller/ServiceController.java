package com.tsl.urlshortner.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsl.urlshortner.dto.ShortUrlResponseDTO;
import com.tsl.urlshortner.dto.UrlMappingRequestDto;
import com.tsl.urlshortner.dto.UrlMappingResponseDto;
import com.tsl.urlshortner.service.USAService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1") 
@Slf4j
public class ServiceController {
	
	private final USAService service;
	
	public ServiceController(USAService service) {
		this.service = service;
	}

	@PostMapping("/generate")
	public ResponseEntity<ShortUrlResponseDTO> get(@RequestBody UrlMappingRequestDto input) {
		log.info("Request for generate short url : {} ", input);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName(); 
		return ResponseEntity.ok(service.encodeString(input, username));
	}
	
	@GetMapping("/get")
	public ResponseEntity<List<UrlMappingResponseDto>> getAll(){
		log.info("Inside get all method");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName(); 
		return ResponseEntity.ok(service.findAll(username));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable long id){
		log.info("Delete request for id : {} ", id);
		System.out.println("Tanmay in delete method");
		return ResponseEntity.ok(service.delete(id));
	}
}
