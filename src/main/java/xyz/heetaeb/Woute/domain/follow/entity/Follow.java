package xyz.heetaeb.Woute.domain.follow.entity;

import jakarta.persistence.*;
import lombok.*;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;

import java.time.ZonedDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "w_follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "follow_seq")
    @SequenceGenerator(name = "follow_seq", sequenceName = "follow_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "following_id")
    private UserEntity following;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "follower_id")
    private UserEntity follower;

    private boolean followState;
    private ZonedDateTime createdAt;

}
