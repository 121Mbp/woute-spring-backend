package xyz.heetaeb.Woute.domain.chat.dto;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;

@Data
@Builder
public class ChatListResponseDTO {
	private UserEntity myUser;
	private List<Room> rooms;
	
	@Builder
	@Getter
	@AllArgsConstructor
	public static class Room {
		private Long toUserId;
		
		private String toUserNick;
		
		private String toUserImg;
		
		private Long roomId;
		
		private Boolean isRead;
		
		private String lastMsg;
		
		private ZonedDateTime lastMsgTime;
	}
}
