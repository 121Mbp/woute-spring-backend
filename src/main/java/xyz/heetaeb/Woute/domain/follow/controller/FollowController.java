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
    
    @GetMapping("/{id}/follower")
    public List<SimpleFollowerListDTO> getFollower(@PathVariable("id") Long userid) {
    	System.out.println("userid : " + userid);
    	return followService.getFollowers(userid);
    }
    
    @GetMapping("/{id}/following")
    public List<SimpleFollowingListDTO> getFollowing(@PathVariable("id") Long userid) {
    	return followService.getFollowings(userid);
    }
    
    @DeleteMapping("/follow/{id}")
    public void unFollow(@PathVariable("id") Long id) {
    	followService.unFollow(id);
    }
    
    @PostMapping("/{id}/follower/search")
    public List<SimpleFollowerListDTO> searchFollower(@PathVariable("id") Long userid, @RequestBody RequestDTO dto) {
    	return followService.searchFollower(userid, dto.getNickname());
    }
    
    @PostMapping("/{id}/following/search")
    public List<SimpleFollowingListDTO> searchFollowing(@PathVariable("id") Long userid, @RequestBody RequestDTO dto) {
    	return followService.searchFollowing(userid, dto.getNickname());
    }
}
