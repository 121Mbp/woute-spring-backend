package xyz.heetaeb.Woute.domain.feed.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "w_attach")
public class AttachEntity {
    @Id
    private String uuid;
    private Long feedId;
    private String origin;
    private String type;
    private String filePath;

    @Builder
    public AttachEntity(String uuid, Long feedId, String origin, String type, String filePath) {
        this.uuid = uuid;
        this.feedId = feedId;
        this.origin = origin;
        this.type = type;
        this.filePath = filePath;
    }
}
