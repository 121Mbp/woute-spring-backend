package xyz.heetaeb.Woute.domain.chat.dto;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatLogResponseDTO {
	private List<Message> Messages;
	

	@Builder
	@Getter
	@AllArgsConstructor
	public static class Message {
		private Long id;
		
		private Long userid;
		
		private String profileImg;
		
		private Long roomId;
		
		private String content;
		
		private ZonedDateTime createdAt;
	}
}
