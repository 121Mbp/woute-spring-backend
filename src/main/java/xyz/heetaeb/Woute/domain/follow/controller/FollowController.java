package xyz.heetaeb.Woute.domain.follow.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import xyz.heetaeb.Woute.domain.follow.dto.RequestDTO;
import xyz.heetaeb.Woute.domain.follow.dto.SimpleFollowerListDTO;
import xyz.heetaeb.Woute.domain.follow.dto.SimpleFollowingListDTO;
import xyz.heetaeb.Woute.domain.follow.service.FollowService;


@RequiredArgsConstructor
@RestController
public class FollowController {
    private final FollowService followService;

    // 팔로우
    @PostMapping("/follow")
    @Transactional
    public ResponseEntity<String> follow(@RequestBody RequestDTO dto) {
        try {
        	System.out.println("성공");
            followService.createFollow(dto);
            return ResponseEntity.ok("팔로우 성공");
        } catch (Exception e) {
        	System.out.println("실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("에러" + e.getMessage());
        }
    }
    
    // 언팔로우
    @DeleteMapping("/follow/{id}")
    public void unFollow(@PathVariable("id") Long id) {
    	followService.unFollow(id);
    }
    
    // 팔로워 리스트
    @PostMapping("/{id}/follower")
    public List<SimpleFollowerListDTO> getFollower(@PathVariable("id") Long userId, @RequestBody RequestDTO dto) {
    	System.out.println("userid : " + userId);
    	return followService.getFollowers(userId, dto.getFollowingId());
    }
    
    // 팔로잉 리스트
    @PostMapping("/{id}/following")
    public List<SimpleFollowingListDTO> getFollowing(@PathVariable("id") Long userId, @RequestBody RequestDTO dto) {
    	return followService.getFollowings(userId, dto.getFollowingId());
    }
    
    
    // 팔로워 리스트 검색
    @PostMapping("/{id}/follower/search")
    public List<SimpleFollowerListDTO> searchFollower(@PathVariable("id") Long userid, @RequestBody RequestDTO dto) {
    	return followService.searchFollower(userid, dto.getFollowingId(), dto.getNickname());
    }
    
    // 팔로잉 리스트 검색
    @PostMapping("/{id}/following/search")
    public List<SimpleFollowingListDTO> searchFollowing(@PathVariable("id") Long userid, @RequestBody RequestDTO dto) {
    	return followService.searchFollowing(userid, dto.getFollowingId(), dto.getNickname());
    }
    
    // 유저 페이지에서 언팔할때 follow_id가 없어서 조회 후 id 값 추출
    @GetMapping("check/{myId}/{userId}")
    public SimpleFollowerListDTO checkUnFollow(@PathVariable("myId") Long myId, @PathVariable("userId") Long userId) {
    	return followService.getFollower(myId, userId);
    }
}
