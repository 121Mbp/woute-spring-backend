package xyz.heetaeb.Woute.domain.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttachRequest {
    private String uuid;
    private String origin;
}
