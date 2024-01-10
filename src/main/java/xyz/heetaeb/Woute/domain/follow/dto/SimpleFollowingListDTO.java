package xyz.heetaeb.Woute.domain.follow.dto;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleFollowingListDTO {
	private Long id;
	private Long followingId;
	private String followingNick;
	private String followingImg;
	private ZonedDateTime CreatedAt;
	private boolean followState;
}
