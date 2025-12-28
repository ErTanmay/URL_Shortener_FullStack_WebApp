package com.tsl.urlshortner.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsl.urlshortner.service.USAService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/") 
@Slf4j
public class RedirectController {

	private final USAService service;

	public RedirectController(USAService service) {
		super();
		this.service = service;
	}
	
	@GetMapping("{shortUrl}")
	public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortUrl) {
		
		log.info("Short url is : {}", shortUrl);
		
        String originalUrl = service.getLongUrl(shortUrl);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(java.net.URI.create(originalUrl));

        log.info("Header is : {}", headers);
        
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302

        }
}
