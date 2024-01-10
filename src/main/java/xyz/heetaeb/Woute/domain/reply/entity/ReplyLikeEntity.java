package xyz.heetaeb.Woute.domain.reply.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "w_reply_like")
public class ReplyLikeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "w_reply_like_seq")
	@SequenceGenerator(name = "w_reply_like_seq",sequenceName = "w_reply_like_SEQ",allocationSize = 1)
	private Long id;
	private Long replyId;
	private Long userId;
	
}
