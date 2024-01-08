package xyz.heetaeb.Woute.domain.notification;

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
	
	private boolean read;
	
	private ZonedDateTime CreatedAt;
	
	public static NotiRespDTO toDto(Notification notification) {
		return new NotiRespDTO(
				notification.getId(), 
				notification.getUserId(), 
				notification.getNickname(),
				notification.getProfileImg(),
				notification.getContent(), 
				notification.getSenderUrl(), 
				notification.isRead(), 
				notification.getCreatedAt());
	}
}
