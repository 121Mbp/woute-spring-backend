package xyz.heetaeb.Woute.domain.chat.dto;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindUserListDTO {
	private List<User> users;
	
	@Builder
	@Getter
	@AllArgsConstructor
	public static class User {
		public Long userId;
		public Long roomId;
		public String profileImg;
		public String nickName;
		
	}
}
