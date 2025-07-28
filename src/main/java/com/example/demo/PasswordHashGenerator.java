package com.example.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "password1234";
		String hashedPassword = encoder.encode(rawPassword);
		System.out.println("ハッシュ化されたパスワード" + hashedPassword);
	}
}
