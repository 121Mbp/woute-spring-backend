package xyz.heetaeb.Woute.domain.chat.dto;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class ChatRequestDTO {
	private Long myId;
	
	private Long toUserId;
	
	private Long roomId;
	
	private String message;
	
	private ZonedDateTime lastMsgTime;
	
	private String nickName;
}
