package xyz.heetaeb.Woute.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	private Long id;
	private String email;
	private String nickname;
	private String introduction;
	private String profileImage;
	private String password;

	public static UserResponse of(UserEntity user) {
		return UserResponse.builder()
				.id(user.getId())
				.email(user.getEmail())
				.nickname(user.getNickname())
				.build();
	}
	 public static UserResponse from(UserEntity userEntity) {
	        UserResponse userResponse = new UserResponse();
	        userResponse.setId(userEntity.getId());
	        userResponse.setNickname(userEntity.getNickname());
	        userResponse.setIntroduction(userEntity.getIntroduction());
	        userResponse.setProfileImage(userEntity.getProfileImage());
	        userResponse.setPassword(userEntity.getPassword());
	        return userResponse;
	    }
}