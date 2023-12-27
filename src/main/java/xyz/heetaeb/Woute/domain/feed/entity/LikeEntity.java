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
@Table(name = "w_like")
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_seq")
    @SequenceGenerator(name = "like_seq", sequenceName = "like_SEQ", allocationSize = 1)
    private Long id;
    private Long userId;
    private Long feedId;
    private String nickname;
    private String profileImage;
    private ZonedDateTime createdAt;
}
