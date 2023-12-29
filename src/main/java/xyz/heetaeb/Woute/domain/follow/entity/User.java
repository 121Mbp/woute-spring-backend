package xyz.heetaeb.Woute.domain.follow.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "w_user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "w_user_seq")
    @SequenceGenerator(name = "w_user_seq", sequenceName = "w_user_SEQ", allocationSize = 1)
    private Long id;
    private String nickName;
    private String profileImg;
    
}
