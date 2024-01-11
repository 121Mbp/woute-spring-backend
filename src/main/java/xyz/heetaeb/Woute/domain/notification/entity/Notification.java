package xyz.heetaeb.Woute.domain.notification.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "w_notification")
public class Notification {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    @SequenceGenerator(name = "notification_seq", sequenceName = "notification_seq", allocationSize = 1)
	private Long id;
	
	private Long userId;
	
	private Long toUserId;
	
	private String content;
	
	private String senderUrl;
	
	private String type;
	
	private boolean read;
	
	private ZonedDateTime createdAt;
}
