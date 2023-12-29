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

    @Operation(summary = "피드 목록")
    @GetMapping("/p")
    public List<FeedResponse> getAllFeeds() {
        return feedService.feedList();
    }

    @Operation(summary = "피드 상세페이지")
    @GetMapping("/p/{id}")
    public CourseResponse getCourseList(@PathVariable Long id) {
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
    public void modifyFeedPost(@PathVariable Long id, @RequestBody FeedRequest request) {
        feedService.modifyFeed(id, request);
    }

    @Operation(summary = "피드 삭제")
    @DeleteMapping("/p/{id}")
    public void deleteFeedPost(@PathVariable Long id) {
        feedService.deleteFeed(id);
    }

    @Operation(summary = "좋아요")
    @PutMapping("/p/{id}/like")
    public void likeFeedPost(@PathVariable Long id, @RequestBody LikeRequest request) {
        feedService.likeFeed(id, request);
    }

    @Operation(summary = "좋아요 취소")
    @PutMapping("/p/{id}/like/{likeId}")
    public void unlikeFeedPost(@PathVariable Long id, @PathVariable Long likeId, @RequestBody LikeRequest request) {
        feedService.unlikeFeed(id, likeId, request);
    }

}
