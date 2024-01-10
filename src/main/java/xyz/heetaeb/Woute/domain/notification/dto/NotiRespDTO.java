package xyz.heetaeb.Woute.domain.notification.dto;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Data;
import xyz.heetaeb.Woute.domain.notification.entity.Notification;

@Builder
@Data
public class NotiRespDTO {
	private Long id;
	
	private Long userId;
	
	private String nickname;
	
	private String profileImg;
	
	private String content;
	
	private String senderUrl;
	
	private String type;
	
	private boolean isRead;
	
	private ZonedDateTime CreatedAt;
	
	
	public static NotiRespDTO toDto(Notification notification) {
		return new NotiRespDTO(
				notification.getId(), 
				notification.getUserId(), 
				notification.getNickname(),
				notification.getProfileImg(),
				notification.getContent(), 
				notification.getSenderUrl(), 
				notification.getType(),
				notification.isRead(), 
				notification.getCreatedAt());
	}
}
