package com.tsl.urlshortner.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlResponseDTO {

	private String longUrl;
	private String shortUrl;
	
}
