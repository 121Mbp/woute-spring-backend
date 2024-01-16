package xyz.heetaeb.Woute.domain.feed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private Long userId;
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
    private List<Like> likes;
    private List<Tag> tags;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Course {
        private Long id;
        private String code;
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
    public static class Tag {
        private Long id;
        private String words;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Attach {
        private String uuid;
        private String origin;
        private String type;
        private String filePath;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Like {
        private Long id;
        private Long userId;
        private String nickname;
        private String profileImage;
    }
}
