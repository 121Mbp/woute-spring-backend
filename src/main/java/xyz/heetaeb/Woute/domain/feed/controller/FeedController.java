package xyz.heetaeb.Woute.domain.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.heetaeb.Woute.domain.feed.dto.request.FeedRequest;
import xyz.heetaeb.Woute.domain.feed.dto.request.LikeRequest;
import xyz.heetaeb.Woute.domain.feed.dto.response.CourseResponse;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse;
import xyz.heetaeb.Woute.domain.feed.service.FeedService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @Operation(summary = "피드 목록")
    @GetMapping("/p")
    public List<FeedResponse> getFeedList() {
        return feedService.feedList();
    }

    @Operation(summary = "피드 상세페이지")
    @GetMapping("/p/{id}")
    public CourseResponse getCourseList(@PathVariable Long id) {
        return feedService.courseList(id);
    }

    @Operation(summary = "피드 등록")
    @PostMapping("/p")
    public ResponseEntity<String> insertFeedPost(@RequestBody FeedRequest request) {
        try {
            feedService.insertFeed(request);
            return ResponseEntity.ok("피드가 정상적으로 등록 되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("피드가 등록되지 않았습니다. " + e.getMessage());
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
