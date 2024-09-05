package com.example.oba.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecurityUtils {

	private static SecurityUtils instance;
	private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private SecurityUtils() {

	}

	public static SecurityUtils getInstance() {
		if (instance == null) {
			instance = new SecurityUtils();
		}
		return instance;
	}

	public String encode(String text) {
		return passwordEncoder.encode(text);
	}

	public boolean match(String text, String enceryptedText) {
		return passwordEncoder.matches(text, enceryptedText);
	}
}
