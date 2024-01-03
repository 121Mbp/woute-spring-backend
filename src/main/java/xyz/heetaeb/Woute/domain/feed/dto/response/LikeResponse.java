package xyz.heetaeb.Woute.domain.feed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {
    private Long id;
    private Long userId;
    private Long feedId;
    private String nickname;
    private String profileImage;
}
