package xyz.heetaeb.Woute.domain.reply.dto.response;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
public class ReplyResponse {
	private Long id;
	private Long feed_id;
	private Long reply_id;
	private Long user_id;
	private String nickname;
	private String profileImage;
	private String content;
	private Integer heartCount;
	private ZonedDateTime createdAt;
	private Boolean userLiked;
}
