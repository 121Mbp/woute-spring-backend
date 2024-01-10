package xyz.heetaeb.Woute.domain.reply.dto.request;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRequest {
	private Long feed_id;
	private Long reply_id;
	private Long user_id;
	private String nickname;
	private String profileImage;
	private Integer heartCount;
	private String content;
	private ZonedDateTime createdAt;
}
