package xyz.heetaeb.Woute.domain.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {
    private String store;
    private String address;
    private String phone;
    private String homepage;
    private String category;
    private String latitude;
    private String longitude;
}
