package com.tsl.urlshortner.exception;

import org.jspecify.annotations.Nullable;

public class EmailNotFoundException extends RuntimeException{
	
	public EmailNotFoundException(@Nullable String msg) {
		super(msg);
	}
}
