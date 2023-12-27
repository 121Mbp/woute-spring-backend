package xyz.heetaeb.Woute.domain.feed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
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
    private List<Course> courses;
    private List<Attach> attaches;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Course {
        private Long id;
        private String store;
        private String address;
        private String phone;
        private String homepage;
        private String category;
        private String latitude;
        private String longitude;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Attach {
        private String uuid;
        private String origin;
    }
}
