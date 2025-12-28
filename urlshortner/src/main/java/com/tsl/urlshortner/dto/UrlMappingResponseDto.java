package com.tsl.urlshortner.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlMappingResponseDto {
	
	private long id;
	private String longUrl;
	private String shortUrl;
	private long count;

}
