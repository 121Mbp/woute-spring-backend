package xyz.heetaeb.Woute.domain.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedRequest {
    private Long userId;
    private String nickname;
    private String profileImage;
    private String type;
    private String title;
    private String content;
    private String hashtag;
    private int heartCount;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    private List<CourseRequest> course;
    private List<AttachRequest> attach;
}
