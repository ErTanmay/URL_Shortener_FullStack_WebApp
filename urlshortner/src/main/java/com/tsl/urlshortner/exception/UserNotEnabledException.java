package com.tsl.urlshortner.exception;

import org.jspecify.annotations.Nullable;

public class UserNotEnabledException extends RuntimeException{
	
	public UserNotEnabledException(@Nullable String msg) {
		super(msg);
	}
}
