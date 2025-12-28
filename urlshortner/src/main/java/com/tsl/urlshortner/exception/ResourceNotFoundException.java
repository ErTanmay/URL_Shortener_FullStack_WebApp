package com.tsl.urlshortner.exception;

import org.jspecify.annotations.Nullable;

public class ResourceNotFoundException extends RuntimeException{
	
	public ResourceNotFoundException(@Nullable String msg) {
		super(msg);
	}
}
