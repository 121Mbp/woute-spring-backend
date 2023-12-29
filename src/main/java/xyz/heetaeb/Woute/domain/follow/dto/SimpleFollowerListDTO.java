package xyz.heetaeb.Woute.domain.follow.dto;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleFollowerListDTO {
	private Long id;
	private Long followerId;
	private String followerNick;
	private String followerImg;
	private ZonedDateTime CreatedAt;
	private boolean followState;
}
