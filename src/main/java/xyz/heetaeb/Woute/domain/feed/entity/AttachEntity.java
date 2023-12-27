package xyz.heetaeb.Woute.domain.feed.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "w_attach")
public class AttachEntity {
    @Id
    @Builder.Default
    private String uuid = UUID.randomUUID().toString();
    private Long feedId;
    private String origin;
}
