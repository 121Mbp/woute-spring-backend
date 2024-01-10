package xyz.heetaeb.Woute.domain.search.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse.Attach;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse.Course;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse.Like;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse.Tag;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResultDTO {
	private Long id;
	private String type;
	private Integer heartCount;
	private Integer replyCount;
    private List<Course> courses;
    private List<Tag> tags;
    private List<Attach> attaches;
    private List<Like> likes;
	
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
