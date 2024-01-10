package xyz.heetaeb.Woute.domain.user.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.user.dto.response.UserResponse;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;
import xyz.heetaeb.Woute.global.config.SecurityUtil;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserResponse getMyInfo() {
		return userRepository.findById(SecurityUtil.getCurrentMemberId())
				.map(UserResponse::of)
				.orElseThrow(()-> new RuntimeException("유저 정보가 없습니다."));
	}
	 @Transactional
	    public UserResponse changeMemberPassword(String email, String exPassword, String newPassword) {
	        UserEntity user = userRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
	        if (!passwordEncoder.matches(exPassword, user.getPassword())) {
	            throw new RuntimeException("비밀번호가 맞지 않습니다");
	        }
	        user.setPassword(passwordEncoder.encode((newPassword)));
	        return UserResponse.of(userRepository.save(user));

	 }
	 public UserEntity findById(Long userId) {
			return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
		}
	 
	   
}