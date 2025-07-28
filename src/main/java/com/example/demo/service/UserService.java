package com.example.demo.service;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public void register(User user) {
        // パスワードをハッシュ化してから保存
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userRepository.save(user);
    }
	
	//メールアドレスからユーザー検索
	public User findByEmail(String email) {
	    return userRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));
	}
}