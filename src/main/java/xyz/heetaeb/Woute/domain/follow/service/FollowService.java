package xyz.heetaeb.Woute.domain.follow.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.heetaeb.Woute.domain.follow.dto.RequestDTO;
import xyz.heetaeb.Woute.domain.follow.dto.SimpleFollowerListDTO;
import xyz.heetaeb.Woute.domain.follow.dto.SimpleFollowingListDTO;
import xyz.heetaeb.Woute.domain.follow.entity.Follow;
import xyz.heetaeb.Woute.domain.follow.repository.FollowRepository;
import xyz.heetaeb.Woute.domain.notification.service.NotiService;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotiService notiService;
    
    // 팔로우
    @Transactional
    public void createFollow(RequestDTO dto) {
        UserEntity followingUser = userRepository.findById(dto.getFollowingId())
        		.orElseThrow();
        UserEntity followerUser = userRepository.findById(dto.getFollowerId())
        		.orElseThrow();
        
        	// 상대방이 나를 팔로우 한 상태일때 상대방 followState true로 전환
        	if(followRepository.countByFollowingIdAndFollowerId(dto.getFollowerId(), dto.getFollowingId()) == 1) {
        		Follow fromFollow = followRepository.findByFollowingIdAndFollowerId(dto.getFollowerId(), dto.getFollowingId());
        		fromFollow  = Follow.builder()
        				.id(fromFollow.getId())
        				.following(fromFollow.getFollowing())
        				.follower(fromFollow.getFollower())
        				.followState(true)
        				.createdAt(ZonedDateTime.now())
        				.build();
        		followRepository.save(fromFollow);
        		
        		Follow tooFollow = Follow.builder()
        				.following(followingUser)
        				.follower(followerUser)
        				.followState(true)
        				.createdAt(ZonedDateTime.now())
        				.build();
        		followRepository.save(tooFollow);
        		
        		// 팔로우 알림 전송
        		notiService.send(tooFollow.getFollower().getId(),
        				tooFollow.getFollowing().getId(),
        				"님이 팔로우 했습니다",
        				"/users/" + tooFollow.getFollowing().getId(),
        				"follow");
        	} else {
        		// 상대방 팔로우가 없을 경우
        		Follow follow = Follow.builder()
        				.following(followingUser)
        				.follower(followerUser)
        				.followState(false)
        				.createdAt(ZonedDateTime.now())
        				.build();
        		followRepository.save(follow);
        		// 팔로우 알림 전송
        		notiService.send(
        				follow.getFollower().getId(),
        				follow.getFollowing().getId(),
        				"님이 팔로우 했습니다", 
        				"/users/" + follow.getFollowing().getId(),
        				"follow");
        	}
    }
    
    // 언팔로우
    public void unFollow(Long id) {
    	Follow follow = followRepository.findById(id).orElseThrow();
    	
    	// 맞팔로우 상태에서 언팔할때 다른 한쪽 followStata false 전환
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
    public List<SimpleFollowerListDTO> getFollowers(Long userId, Long myId) {
        List<Follow> followers = followRepository.findByFollowerId(userId);
        
        return followers.stream().map((follower) -> SimpleFollowerListDTO.builder()
        		.id(follower.getId())
        		.followerId(follower.getFollowing().getId())
        		.followerNick(follower.getFollowing().getNickname())
        		.followerImg(follower.getFollowing().getProfileImage())
        		.CreatedAt(follower.getCreatedAt())
        		.followState(
        				userId == myId ? follower.isFollowState() :
    					followRepository.countByFollowingIdAndFollowerId(myId, follower.getFollowing().getId()) == 1 ?
    							true : false
    					)
        		.build()).toList();
    }

    // 팔로잉 리스트
    public List<SimpleFollowingListDTO> getFollowings(Long userId, Long myId) {
    	List<Follow> followings = followRepository.findByFollowingId(userId);
    	
    	return followings.stream().map((following) -> SimpleFollowingListDTO.builder()
    			.id(following.getId())
    			.followingId(following.getFollower().getId())
    			.followingNick(following.getFollower().getNickname())
    			.followingImg(following.getFollower().getProfileImage())
    			.CreatedAt(following.getCreatedAt())
    			.followState(
    					userId == myId ? following.isFollowState() :
    					followRepository.countByFollowingIdAndFollowerId(myId, following.getFollower().getId()) == 1 ?
    							true : false
    					)
    			.build()).toList();
    }


    // 팔로워 검색
    public List<SimpleFollowerListDTO> searchFollower(Long userId, Long myId , String nickname) {
    	List<Follow> followers = followRepository.findByFollowerId(userId);
    	
    	// 검색어 공백일때 결과 없음
    	if(nickname.trim().equals("")) {
    		return followers.stream().filter(follower -> follower.getFollowing().getNickname().contains("가"))
    				.map(f -> SimpleFollowerListDTO.builder()
    						.id(f.getId())
    						.followerId(f.getFollowing().getId())
    						.followerNick(f.getFollowing().getNickname())
    						.followerImg(f.getFollowing().getProfileImage())
    						.followState(f.isFollowState())
    						.CreatedAt(f.getCreatedAt())
    						.build()).toList();
    	} else {
    		// 검색어 검색
        	return followers.stream().filter(follower -> follower.getFollowing().getNickname().contains(nickname.trim()))
    		    	.map(f -> SimpleFollowerListDTO.builder()
    		    			.id(f.getId())
    		    			.followerId(f.getFollowing().getId())
    		    			.followerNick(f.getFollowing().getNickname())
    		    			.followerImg(f.getFollowing().getProfileImage())
    		    			.followState(
    		    					userId == myId ? f.isFollowState() :
    		        					followRepository.countByFollowingIdAndFollowerId(myId, f.getFollowing().getId()) == 1 ?
    		        							true : false
    								)
    		    			.CreatedAt(f.getCreatedAt())
    		    			.build()).toList();
    	}
    }
    
    // 팔로잉 검색
    public List<SimpleFollowingListDTO> searchFollowing(Long userId, Long myId , String nickname) {
    	List<Follow> followings = followRepository.findByFollowingId(userId);
    	
    	// 검색어 공백일때 결과 없음
    	if(nickname.trim().equals("")) {
    		return followings.stream().filter(following -> following.getFollower().getNickname().contains("^%"))
    				.map(f -> SimpleFollowingListDTO.builder()
    						.id(f.getId())
    						.followingId(f.getFollower().getId())
    						.followingNick(f.getFollower().getNickname())
    						.followingImg(f.getFollower().getProfileImage())
    						.CreatedAt(f.getCreatedAt())
    						.followState(f.isFollowState())
    						.build()).toList();
    	} else {
    		// 검색어 검색
    		return followings.stream().filter(following -> following.getFollower().getNickname().contains(nickname.trim()))
    				.map(f -> SimpleFollowingListDTO.builder()
    						.id(f.getId())
    						.followingId(f.getFollower().getId())
    						.followingNick(f.getFollower().getNickname())
    						.followingImg(f.getFollower().getProfileImage())
    						.CreatedAt(f.getCreatedAt())
    						.followState(
    								userId == myId ? f.isFollowState() :
    		        					followRepository.countByFollowingIdAndFollowerId(myId, f.getFollower().getId()) == 1 ?
    		        							true : false
    								)
    						.build()).toList();
    	}
    	
    }
    

    /**
     * 유저 페이지에서 언팔할때 follow_id가 없어서 단일조회 후 id 값 추출
     * @param myId
     * @param userId
     * @return
     */
	public SimpleFollowerListDTO getFollower(Long myId, Long userId) {
		Follow follower = followRepository.findByFollowingIdAndFollowerId(myId, userId);
		return SimpleFollowerListDTO.builder().id(follower.getId()).build();
	}
}
