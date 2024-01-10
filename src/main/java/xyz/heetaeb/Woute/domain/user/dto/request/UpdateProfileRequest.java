package xyz.heetaeb.Woute.domain.user.dto.request;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {
	private String nickname;
	private String password;
	private String profileImage;
	private String introduction;
	private ZonedDateTime updatedAt;
}
