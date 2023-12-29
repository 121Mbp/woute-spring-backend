package xyz.heetaeb.Woute.domain.feed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FeedResponse {
    private Long id;
    private String nickname;
    private String profileImage;
    private String type;
    private String title;
    private String content;
    private String hashtag;
    private Integer heartCount;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private List<Attach> attaches;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Attach {
        private String uuid;
        private String origin;
    }
}
