package xyz.heetaeb.Woute.domain.follow.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RequestDTO {
	private Long followingId;
	private Long followerId;
	private String nickname;
}
