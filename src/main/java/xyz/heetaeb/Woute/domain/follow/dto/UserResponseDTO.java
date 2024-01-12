package xyz.heetaeb.Woute.domain.follow.dto;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.heetaeb.Woute.domain.chat.dto.ChatLogResponseDTO;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse.Attach;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse.Course;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse.Like;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse.Tag;
import xyz.heetaeb.Woute.domain.user.dto.response.UserResponse;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
	private Long id;
	private Long roomId;
	private String nickname;
	private String introduction;
	private String ProfileImage;
	private Long feedsCount;
	private Long followerCount;
	private Long followingCount;
	private boolean hasFollowed;
	private List<FeedResponse> feeds;
}
