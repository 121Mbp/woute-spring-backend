package xyz.heetaeb.Woute.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequestDto {
	private String email;
	private String exPassword;
	private String newPassword;
}
