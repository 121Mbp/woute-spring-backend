package xyz.heetaeb.Woute.domain.user.dto.request;

import java.time.ZonedDateTime;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequest {
	private String email;
	private String password;
	private String nickname;
	private String provider;
	private String profileImage;
	
	public UserEntity toUser(PasswordEncoder passwordEncoder) {
		UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .profileImage("static/basicProfil.png")
                .provider("woute")
                .build();
        System.out.println("UserEntity: " + userEntity);
        return userEntity;
	}
	public UserEntity tolog(PasswordEncoder passwordEncoder) {
		UserEntity userEntity = UserEntity.builder()
				.email(email)
				.password(passwordEncoder.encode(password))
				.build();
		System.out.println("UserEntity: " + userEntity);
		return userEntity;
	}
	
	public UsernamePasswordAuthenticationToken toAuthentication() {
		return new UsernamePasswordAuthenticationToken(email, password);
	}
}
