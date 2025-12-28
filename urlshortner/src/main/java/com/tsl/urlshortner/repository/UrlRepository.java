package com.tsl.urlshortner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.tsl.urlshortner.model.UrlMapping;

import jakarta.persistence.LockModeType;


@Repository
public interface UrlRepository extends JpaRepository<UrlMapping, Long>{
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<UrlMapping> findByShortUrl(String shortUrl);
	
	List<UrlMapping> findByUserUsername(String userName);

}
