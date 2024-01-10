package xyz.heetaeb.Woute.domain.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequest {
    private Long userId;
    private String nickname;
    private String profileImage;
    private ZonedDateTime createdAt;
}
