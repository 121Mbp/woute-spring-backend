package xyz.heetaeb.Woute.domain.reply.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.feed.dto.request.LikeRequest;
import xyz.heetaeb.Woute.domain.reply.dto.request.ReplyLikeRequest;
import xyz.heetaeb.Woute.domain.reply.dto.request.ReplyRequest;
import xyz.heetaeb.Woute.domain.reply.dto.response.ReplyResponse;
import xyz.heetaeb.Woute.domain.reply.entity.ReplyEntity;
import xyz.heetaeb.Woute.domain.reply.service.ReplyService;

@RestController
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService replyService;
    
//	@Operation(summary = "댓글 목록")
//	@GetMapping("p/{feed_id}/reply")
//	public List<ReplyResponse> getReplyList(@PathVariable("feed_id") Long feed_id){
//		return replyService.replyList(feed_id);
//		
//	}
	
	@Operation(summary = "댓글 목록")
	@GetMapping("/p/{feedId}/reply")
	public List<ReplyResponse> getReplyList(@PathVariable("feedId") Long feedId,
	                                        @RequestParam("userId") Long userId) {
		System.out.println("userId 들어왔니??"+ userId);
	    return replyService.replyList(feedId, userId);
	}
	
	
	
	@Operation(summary = "댓글등록")
	@PostMapping("/p/{feed_id}/reply")
	public ResponseEntity<String> insertReplyPost(@RequestBody ReplyRequest request){
		try {
			replyService.insertReply(request);
			return ResponseEntity.ok("댓글이 정상적으로 등록 되었습니다");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글이등록되지않았습니다");
		}
	}
	@Operation(summary = "댓글삭제")
	@CrossOrigin
	@DeleteMapping("p/{feed_id}/{reply_id}")
	public void deleteReplyPost(@PathVariable("feed_id") Long feed_id,@PathVariable("reply_id") Long reply_id) {
	    replyService.deleteReply(feed_id,reply_id);
	}
	
	@Operation(summary = "댓글좋아요")
	@PutMapping("p/{feedId}/{replyId}/like")
	public void likeReplyPost(@PathVariable("feedId") Long feedId
			,@PathVariable("replyId") Long replyId
			,@RequestBody ReplyLikeRequest request) {
			replyService.replyLike(feedId, replyId, request);
	}
//	
	@Operation(summary = "댓글 좋아요 취소")
    @DeleteMapping("/p/{feedId}/{replyId}/{userId}/like")
    public void cancelReplyLike(@PathVariable("feedId") Long feedId, 
                                @PathVariable("replyId") Long replyId, 
                                @PathVariable("userId") Long userId) {
        replyService.replyLikeCancel(feedId, replyId, userId);
    }
}
	
	
