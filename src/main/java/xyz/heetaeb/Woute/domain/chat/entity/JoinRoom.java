package xyz.heetaeb.Woute.domain.chat.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity(name = "w_joinroom")
public class JoinRoom {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "joinroom_seq")
    @SequenceGenerator(name = "joinroom_seq", sequenceName = "joinroom_seq", allocationSize = 1)
	private Long id;
	
	private Long myUserId;
	
	private Long toUserId;
	
	private String toUserNick;
	
	private String toUserImg;
	
	private Long roomId;
	
	private String lastMsg;
	
	private Boolean read;
	
	private ZonedDateTime lastMsgTime;
}
