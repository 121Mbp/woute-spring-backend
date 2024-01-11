package xyz.heetaeb.Woute.domain.reply.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.feed.dto.request.LikeRequest;
import xyz.heetaeb.Woute.domain.feed.entity.FeedEntity;
import xyz.heetaeb.Woute.domain.feed.repository.FeedRepository;
import xyz.heetaeb.Woute.domain.notification.service.NotiService;
import xyz.heetaeb.Woute.domain.reply.dto.request.ReplyLikeRequest;
import xyz.heetaeb.Woute.domain.reply.dto.request.ReplyRequest;
import xyz.heetaeb.Woute.domain.reply.dto.response.ReplyResponse;
import xyz.heetaeb.Woute.domain.reply.entity.ReplyEntity;
import xyz.heetaeb.Woute.domain.reply.entity.ReplyLikeEntity;
import xyz.heetaeb.Woute.domain.reply.repository.ReplyLIkeRepository;
import xyz.heetaeb.Woute.domain.reply.repository.ReplyRepository;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ReplyService {
	
	
	private final ReplyRepository replyRepository;
	private final ReplyLIkeRepository likeRepository;
	private final NotiService notiService;
	private final UserRepository userRepository;
	private final FeedRepository feedRepository;
	
//	댓글 목록 
//	public List<ReplyResponse> replyList(Long feed_id) {
//		List<ReplyEntity> replys = replyRepository.findByFeedId(feed_id);
//
//		return replys.stream()
//				.map((reply) -> ReplyResponse.builder().feed_id(reply.getFeedId()).reply_id(reply.getId())
//						.nickname(reply.getNickname()).profileImage(reply.getProfileImage()).content(reply.getContent())
//						.createdAt(reply.getCreated_at()).heartCount(reply.getHeartCount()).build())
//				.toList();
//	}
	public List<ReplyResponse> replyList(Long feedId, Long userId){
		List<ReplyEntity> replys = replyRepository.findByFeedId(feedId);
		List<ReplyLikeEntity> userLikes = likeRepository.findByUserId(userId);

		Set<Long> likedReplyIds = userLikes.stream()
				.map(ReplyLikeEntity::getReplyId)
				.collect(Collectors.toSet());
		System.out.println("testetetetet");
		return replys.stream()
				.map(reply -> {
					boolean userLiked = likedReplyIds.contains(reply.getId());
					Optional<UserEntity> user = userRepository.findById(reply.getUserId());
					 System.out.println("Reply ID: " + reply.getId() + " User Liked: " + userLiked);
					return ReplyResponse.builder()
							.id(reply.getId())
							.feed_id(reply.getFeedId())
							.user_id(reply.getUserId())
							.nickname(user.map(UserEntity::getNickname).orElse(null))
							.profileImage(user.map(UserEntity::getProfileImage).orElse(null))
							.content(reply.getContent())
							.createdAt(reply.getCreated_at())
							.heartCount(reply.getHeartCount())
							.userLiked(userLiked)
							.build();
				})
				.collect(Collectors.toList());
	} 
	    
	  
	
	
	//댓글 입력
	@Transactional
	public void insertReply(ReplyRequest request) {
		FeedEntity feed = feedRepository.findById(request.getFeed_id()).orElseThrow();
		Optional<UserEntity> user = userRepository.findById(request.getUser_id());
		
		ReplyEntity reply = ReplyEntity.builder()
				.feedId(request.getFeed_id())
				.userId(request.getUser_id())
				.nickname(user.map(UserEntity::getNickname).orElse(null))
				.profileImage(user.map(UserEntity::getProfileImage).orElse(null))
				.content(request.getContent())
				.heartCount(request.getHeartCount())
				.created_at(ZonedDateTime.now())
				.build();
		replyRepository.save(reply);
		
		// 댓글 등록 알림(자신 제외)
		if(feed.getUserId() != request.getUser_id()) {
			notiService.send(feed.getUserId(),
					request.getUser_id()
					,"님이 댓글을 작성했습니다.", 
					"/p/"+ feed.getId(), 
					feed.getType());
		}

		
	}
	
	//댓글 삭제
	 @Transactional
	    public void deleteReply(Long feed_id, Long reply_id) {
	        // 특정 feedId와 replyId에 해당하는 댓글 찾기
	        ReplyEntity reply = replyRepository.findByFeedIdAndId(feed_id, reply_id)
	                .orElseThrow();
	        replyRepository.delete(reply);
	    }
	 
	 //댓글좋아요
	 @Transactional
	 public void replyLike(Long feed_id, Long reply_id,ReplyLikeRequest request) {
		FeedEntity feed = feedRepository.findById(feed_id).orElseThrow();
		 ReplyEntity reply = replyRepository.findByFeedIdAndId(feed_id, reply_id).orElseThrow();
		 int increase = reply.getHeartCount() + 1;
		 reply.changeFeedLike(increase);
		 replyRepository.save(reply);
		 
		 ReplyLikeEntity like = ReplyLikeEntity.builder()
				 .replyId(request.getReplyId())
				 .userId(request.getUserId())
				 .build();
				 likeRepository.save(like);
				 
				 // 댓글 좋아요 알림(자신 제외)
				 if(reply.getUserId() != request.getUserId()) {
					 notiService.send(reply.getUserId(), 
							 request.getUserId(),
							 "님이 댓글에 좋아요를 눌렀습니다." ,
							 "/p/"+ feed_id,
							 feed.getType());
				 }
	}
	 
	 
	 
	 
	 
	 //좋아요취소
//	 @Transactional
//	 public void replyLikeCancel(Long feed_id, Long reply_id) {
//	     ReplyEntity reply = replyRepository.findByFeedIdAndId(feed_id, reply_id)
//	             .orElseThrow();
//	     int decrease = reply.getHeartCount() -1;
//	     reply.changeFeedLike(decrease);
//	     replyRepository.save(reply);
//	     ReplyLikeEntity likeCancle = likeRepository.findByReplyIdAndUserId(reply_id, reply_id).orElseThrow();
//	     likeRepository.delete(likeCancle);
//	 }
	 
	 @Transactional
	 public void replyLikeCancel(Long feed_id, Long reply_id, Long user_id) {
	     ReplyEntity reply = replyRepository.findByFeedIdAndId(feed_id, reply_id)
	             .orElseThrow();
	     int decrease = reply.getHeartCount() - 1;
	     reply.changeFeedLike(decrease);
	     replyRepository.save(reply);

	     ReplyLikeEntity likeCancel = likeRepository.findByReplyIdAndUserId(reply_id, user_id).orElseThrow();
	     likeRepository.delete(likeCancel);
	 }
	
	
}