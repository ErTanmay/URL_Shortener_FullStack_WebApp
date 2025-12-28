package com.tsl.urlshortner.exception;

import org.jspecify.annotations.Nullable;

public class UserNotFoundException extends RuntimeException{
	
	public UserNotFoundException(@Nullable String msg) {
		super(msg);
	}
}