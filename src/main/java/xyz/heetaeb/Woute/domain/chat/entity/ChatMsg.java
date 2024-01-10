package xyz.heetaeb.Woute.domain.chat.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity(name = "w_message")
public class ChatMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq", sequenceName = "message_seq", allocationSize = 1)
	private Long id;
	
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userid")
	private UserEntity user;
	
	private Long roomId;
	
	private String content;
	
	private ZonedDateTime createdAt;
}
