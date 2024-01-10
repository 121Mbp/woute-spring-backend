package xyz.heetaeb.Woute.domain.reply.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyLikeRequest {
	private Long id;
	private Long replyId;
	private Long userId;
	private String nickname;
	private String profileImage;
}
