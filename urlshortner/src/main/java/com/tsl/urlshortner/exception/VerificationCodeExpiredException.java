package com.tsl.urlshortner.exception;

import org.jspecify.annotations.Nullable;

public class VerificationCodeExpiredException extends RuntimeException{
	
	public VerificationCodeExpiredException(@Nullable String msg) {
		super(msg);
	}
}