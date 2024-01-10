package xyz.heetaeb.Woute.domain.reply.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "w_reply")
public class ReplyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "w_reply_seq")
	@SequenceGenerator(name = "w_reply_seq", sequenceName = "w_reply_SEQ", allocationSize = 1)
	private Long id;
	private Long feedId;
	private Long userId;
	private Long replyId;
	private String nickname;
	private String profileImage;
	private String content;
	private Integer heartCount;
	private ZonedDateTime created_at;

	public void changeFeedLike(int heartCount) {
		this.heartCount = heartCount;
	}
	 public void setHeartCount(int heartCount) {
	        this.heartCount = heartCount;
	    }

}
