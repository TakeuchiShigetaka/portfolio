package com.example.demo.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	// ユーザーをメールアドレスで検索（ログイン時に呼ばれる）
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりませんでした: " + email));

		// Spring Securityの標準Userクラスに変換して返す（UserDetails実装クラス）
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), // ユーザー名の代わりにメールアドレスを使用
				user.getPasswordHash(), // ハッシュ化済みのパスワード
				Collections.emptyList() // 権限リスト（今回は認可機能はなし）
		);

	}
}
