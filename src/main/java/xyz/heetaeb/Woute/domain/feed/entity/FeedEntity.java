package xyz.heetaeb.Woute.domain.feed.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "w_feed")
public class FeedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feed_seq")
    @SequenceGenerator(name = "feed_seq", sequenceName = "feed_SEQ", allocationSize = 1)
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

    public void changeFeedAndCourse(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
        this.updatedAt = ZonedDateTime.now();
    }

    public void changeFeedLike(int heartCount) {
        this.heartCount = heartCount;
    }
}
