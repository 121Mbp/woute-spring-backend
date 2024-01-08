package xyz.heetaeb.Woute.global.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;

public class TokenUtils {
	@Autowired
	private UserRepository userRepository;
	
	  public static Long extractIdFromToken(String token, String secretKey) {
	        Claims claims = Jwts.parser()
	                .setSigningKey(secretKey)
	                .parseClaimsJws(token)
	                .getBody();
	        return Long.parseLong(claims.getSubject());
	    }

	    public static UserEntity getCurrentUserFromToken(String token, String secretKey, UserRepository userRepository) {
	        Long userId = extractIdFromToken(token, secretKey);

	        // 사용자 엔터티 조회
	        return userRepository.findById(userId)
	                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));
	    }
}
