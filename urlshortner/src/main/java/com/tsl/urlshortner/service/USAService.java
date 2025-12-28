package com.tsl.urlshortner.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tsl.urlshortner.dto.ShortUrlResponseDTO;
import com.tsl.urlshortner.dto.UrlMappingRequestDto;
import com.tsl.urlshortner.dto.UrlMappingResponseDto;
import com.tsl.urlshortner.exception.ResourceNotFoundException;
import com.tsl.urlshortner.model.MyUser;
import com.tsl.urlshortner.model.UrlMapping;
import com.tsl.urlshortner.repository.UrlRepository;
import com.tsl.urlshortner.repository.UserRepository;

import io.seruco.encoding.base62.Base62;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class USAService {
	
	Base62 base62 = Base62.createInstance();
	
	private final UserRepository userRepo;
	private final UrlRepository urlRepo;
	private final Environment environment;
	private InetAddress inetAddress;
	
	@Transactional
	public ShortUrlResponseDTO encodeString(UrlMappingRequestDto input, String username) {
		log.info("Url encoding : {}, {}", input, username);
		Optional<MyUser> myUser = userRepo.findByUsername(username);
		UrlMapping urlModel = new UrlMapping();
		
		if(!myUser.isPresent()) {
			throw new UsernameNotFoundException("Username not found");
		}
		
		urlModel.setLongUrl(input.getLongUrl());
		urlModel.setUser(myUser.get());
		
		UrlMapping save = urlRepo.save(urlModel);
		
		String st = new String(String.valueOf(save.getId()));
		
		String encoded = new String(base62.encode(st.getBytes()));
		
		log.info("URL Obj and encoded url : {}, {}", save, encoded);

		save.setShortUrl(encoded);
		UrlMapping save2 = urlRepo.save(save);
		
		ShortUrlResponseDTO urlDto = new ShortUrlResponseDTO();
		urlDto.setLongUrl(save2.getLongUrl());
		urlDto.setShortUrl(createShortUrl(save2.getShortUrl()));
	
		return urlDto;
	}

	
	private String createShortUrl(String short_url) {
		
		String port = environment.getProperty("local.server.port");
		try {
				inetAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
				e.printStackTrace();
		}
		String hostName = inetAddress.getHostName();
		String ipAddress = inetAddress.getHostAddress();
		String url = "http://" + ipAddress + ":" + port + "/" + short_url;

		log.info("The short url is : {}", url);
		
		return url;
	}

	@Transactional
	public String getLongUrl(String shortUrl) {
		log.info("The short url is : {}", shortUrl);
		Optional<UrlMapping> urlMap = urlRepo.findByShortUrl(shortUrl);
		
		if(!urlMap.isPresent()) {
			throw new ResourceNotFoundException("short url not found");
		}
		
		String longUrl = urlMap.get().getLongUrl();
		
		UrlMapping newObj = urlMap.get();
		long clicks = newObj.getClicks();
		newObj.setClicks(clicks +1);
		urlRepo.save(newObj);
		log.info("The long url is : {}", longUrl);
		
		return longUrl;
	}
 
	public List<UrlMappingResponseDto> findAll(String username) {
		
		List<UrlMapping> list = urlRepo.findByUserUsername(username);
		List<UrlMappingResponseDto> newList = new ArrayList<UrlMappingResponseDto>();
		
		for(UrlMapping record : list) {
			newList.add(new UrlMappingResponseDto(record.getId(), record.getLongUrl(), record.getShortUrl(),
					record.getClicks()));
		}

		log.info("The newList is : {}", newList);
		return newList;
	}

	@Transactional
	public String delete(long id) {
		try {
			urlRepo.deleteById(id);
			return "Record with id : " + id + " deleted successfully";
		}catch(Exception e) {
			e.printStackTrace();
			return "an error occured!";
		}
	}


}
