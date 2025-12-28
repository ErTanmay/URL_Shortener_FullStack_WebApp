package com.tsl.urlshortner.exception;

import org.jspecify.annotations.Nullable;

public class InvalidVerificationCodeException extends RuntimeException{
	
	public InvalidVerificationCodeException(@Nullable String msg) {
		super(msg);
	}
}