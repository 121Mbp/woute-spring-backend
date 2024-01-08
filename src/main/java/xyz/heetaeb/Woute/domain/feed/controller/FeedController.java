package xyz.heetaeb.Woute.domain.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.heetaeb.Woute.domain.feed.dto.request.CourseRequest;
import xyz.heetaeb.Woute.domain.feed.dto.request.FeedRequest;
import xyz.heetaeb.Woute.domain.feed.dto.request.LikeRequest;
import xyz.heetaeb.Woute.domain.feed.dto.request.TagsRequest;
import xyz.heetaeb.Woute.domain.feed.dto.response.CourseResponse;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse;
import xyz.heetaeb.Woute.domain.feed.service.FeedService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @Operation(summary = "전체 피드 목록")
    @GetMapping("/p")
    public List<FeedResponse> getAllFeeds() {
        return feedService.feedList();
    }

    @Operation(summary = "코스 목록")
    @GetMapping("/p/courses")
    public List<FeedResponse> getAllCoursesFeeds() {
        return feedService.typeFeedList("courses");
    }

    @Operation(summary = "유저 피드 목록")
    @GetMapping("/users/{userId}/feeds")
    public List<FeedResponse> getAllUserFeeds(@PathVariable("userId") Long userId) {
        return feedService.userFeedList(userId);
    }

    @Operation(summary = "유저 코스 목록")
    @GetMapping("/users/{userId}/course")
    public List<FeedResponse> getAllUserCourseFeeds(@PathVariable("userId") Long userId) {
        return feedService.userCourseFeedList(userId);
    }

    @Operation(summary = "유저 좋아요 목록")
    @GetMapping("/users/{userId}/like")
    public List<FeedResponse> getAllUserFeedsLike(@PathVariable("userId") Long userId) {
        return feedService.getAllUserFeedsLike(userId);
    }

    @Operation(summary = "피드 상세페이지")
    @GetMapping("/p/{id}")
    public CourseResponse getCourseList(@PathVariable("id") Long id) {
        return feedService.courseList(id);
    }

    @Operation(summary = "피드 이미지")
    @GetMapping("/file/{uuid}")
    public ResponseEntity<?> downImage(@PathVariable("uuid") String uuid) throws IOException {
        byte[] downloadImage = feedService.downloadImageSystem(uuid);
        if(downloadImage != null) {
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png"))
                    .body(downloadImage);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "피드 등록")
    @PostMapping("/p")
    public void insertFeedPost(
            @RequestPart(value = "feed") FeedRequest requestFeed,
            @RequestPart(value = "courses", required = false) List<CourseRequest> requestCourses,
            @RequestPart(value = "tags", required = false) List<TagsRequest> requestTags,
            @RequestPart(value = "attaches", required = false) List<MultipartFile> files
    ) throws IOException {
        try {
            feedService.insertFeed(requestFeed, requestCourses, requestTags, files);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Operation(summary = "피드 수정")
    @PutMapping("/p/{id}")
    public void modifyFeedPost(
            @PathVariable("id") Long id,
            @RequestPart(value = "feed") FeedRequest requestFeed,
            @RequestPart(value = "tags", required = false) List<TagsRequest> requestTags
    ) {
        feedService.modifyFeed(id, requestFeed, requestTags);
    }

    @Operation(summary = "피드 삭제")
    @DeleteMapping("/p/{id}")
    public void deleteFeedPost(@PathVariable("id") Long id) {
        feedService.deleteFeed(id);
    }

    @Operation(summary = "좋아요")
    @PutMapping("/p/{feedId}/like")
    public void likeFeedPost(@PathVariable("feedId") Long feedId, @RequestBody LikeRequest request) {
        feedService.likeFeed(feedId, request);
    }

    @Operation(summary = "좋아요 취소")
    @DeleteMapping("/p/{feedId}/like/{likeId}")
    public void unlikeFeedPost(@PathVariable("feedId") Long feedId, @PathVariable("likeId") Long likeId) {
        feedService.unlikeFeed(feedId, likeId);
    }

}
