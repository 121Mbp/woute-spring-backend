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
@Table(name = "w_course")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "w_course_seq")
    @SequenceGenerator(name = "w_course_seq", sequenceName = "w_course_SEQ", allocationSize = 1)
    private Long id;
    private Long feedId;
    private String store;
    private String address;
    private String phone;
    private String homepage;
    private String category;
    private String latitude;
    private String longitude;
}
