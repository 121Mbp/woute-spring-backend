package xyz.heetaeb.Woute.domain.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
public class LikeRequest {
    private String nickname;
    private String profileImage;
    private ZonedDateTime createdAt;
}
