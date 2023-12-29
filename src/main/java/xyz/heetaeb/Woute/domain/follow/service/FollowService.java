package xyz.heetaeb.Woute.domain.follow.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.heetaeb.Woute.domain.follow.dto.RequestDTO;
import xyz.heetaeb.Woute.domain.follow.dto.SimpleFollowerListDTO;
import xyz.heetaeb.Woute.domain.follow.dto.SimpleFollowingListDTO;
import xyz.heetaeb.Woute.domain.follow.entity.Follow;
import xyz.heetaeb.Woute.domain.follow.entity.User;
import xyz.heetaeb.Woute.domain.follow.repository.FollowRepository;
import xyz.heetaeb.Woute.domain.follow.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    // 팔로우
    @Transactional
    public void createFollow(RequestDTO dto) {
        User followingUser = userRepository.findById(dto.getFollowingId())
        		.orElseThrow(()-> new RuntimeException("팔로잉유저가 없습니다"));
        User followerUser = userRepository.findById(dto.getFollowerId())
        		.orElseThrow(()-> new RuntimeException("팔로워유저가 없습니다"));
        
        	if(followRepository.countByFollowingIdAndFollowerId(followerUser.getId(), followingUser.getId()) == 1) {
        		Follow fromFollow = followRepository.findByFollowingIdAndFollowerId(followerUser.getId(), followingUser.getId());
        		fromFollow  = Follow.builder()
        				.id(fromFollow.getId())
        				.following(fromFollow.getFollowing())
        				.follower(fromFollow.getFollower())
        				.followState(true)
        				.createdAt(ZonedDateTime.now())
        				.build();
        		followRepository.save(fromFollow);
        		
        		Follow toofollow = Follow.builder()
        				.following(followingUser)
        				.follower(followerUser)
        				.followState(true)
        				.createdAt(ZonedDateTime.now())
        				.build();
        		followRepository.save(toofollow);
        	} else {
        		Follow follow = Follow.builder()
        				.following(followingUser)
        				.follower(followerUser)
        				.followState(false)
        				.createdAt(ZonedDateTime.now())
        				.build();
        		followRepository.save(follow);
        	}
    }
    
    // 언팔로우
    public void unFollow(Long id) {
    	Follow follow = followRepository.findById(id).orElseThrow();
    	
    	if(followRepository.countByFollowingIdAndFollowerId(follow.getFollower().getId(), follow.getFollowing().getId()) == 1) {
    		Follow fromFollow = followRepository.findByFollowingIdAndFollowerId(follow.getFollower().getId(), follow.getFollowing().getId());
    		fromFollow  = Follow.builder()
    				.id(fromFollow.getId())
    				.following(fromFollow.getFollowing())
    				.follower(fromFollow.getFollower())
    				.followState(false)
    				.createdAt(fromFollow.getCreatedAt())
    				.build();
    		followRepository.save(fromFollow);
    		followRepository.delete(follow);
    	} else {
    		followRepository.delete(follow);
    	}
    }
    


    // 팔로워 리스트
    public List<SimpleFollowerListDTO> getFollowers(Long id) {
        List<Follow> followers = followRepository.findByFollowerId(id);
        
        return followers.stream().map((follower) -> SimpleFollowerListDTO.builder()
        		.id(follower.getId())
        		.followerId(follower.getFollowing().getId())
        		.followerNick(follower.getFollowing().getNickName())
        		.followerImg(follower.getFollowing().getProfileImg())
        		.followState(follower.isFollowState())
        		.CreatedAt(follower.getCreatedAt())
        		.build()).toList();
    }

    // 팔로잉 리스트
    public List<SimpleFollowingListDTO> getFollowings(Long id) {
    	List<Follow> followings = followRepository.findByFollowingId(id);
    	
    	return followings.stream().map((following) -> SimpleFollowingListDTO.builder()
    			.id(following.getId())
    			.followingId(following.getFollower().getId())
    			.followingNick(following.getFollower().getNickName())
    			.followingImg(following.getFollower().getProfileImg())
    			.CreatedAt(following.getCreatedAt())
    			.build()).toList();
    }


    // 팔로워 검색
    public List<SimpleFollowerListDTO> searchFollower(Long userid, String nickname) {
    	List<Follow> followers = followRepository.findByFollowerId(userid);
    	
    	return followers.stream().filter(follower -> follower.getFollowing().getNickName().contains(nickname.trim()))
		    	.map(f -> SimpleFollowerListDTO.builder()
		    			.id(f.getId())
		    			.followerId(f.getFollowing().getId())
		    			.followerNick(f.getFollowing().getNickName())
		    			.followerImg(f.getFollowing().getProfileImg())
		    			.followState(f.isFollowState())
		    			.CreatedAt(f.getCreatedAt())
		    			.build()).toList();
    }
    
    // 팔로잉 검색
    public List<SimpleFollowingListDTO> searchFollowing(Long userid, String nickname) {
    	List<Follow> followings = followRepository.findByFollowingId(userid);
    	
    	
    	return followings.stream().filter(following -> following.getFollower().getNickName().contains(nickname.trim()))
		    	.map(f -> SimpleFollowingListDTO.builder()
		    			.id(f.getId())
		    			.followingId(f.getFollower().getId())
		    			.followingNick(f.getFollower().getNickName())
		    			.followingImg(f.getFollower().getProfileImg())
		    			.CreatedAt(f.getCreatedAt())
		    			.build()).toList();
    }
}
