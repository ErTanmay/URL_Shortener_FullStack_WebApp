package com.tsl.urlshortner.exception;

import org.jspecify.annotations.Nullable;

public class UserAlreadyVerifiedException extends RuntimeException{
	
	public UserAlreadyVerifiedException(@Nullable String msg) {
		super(msg);
	}
}