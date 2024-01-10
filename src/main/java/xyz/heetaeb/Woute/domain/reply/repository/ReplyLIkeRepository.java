package xyz.heetaeb.Woute.domain.reply.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.heetaeb.Woute.domain.reply.entity.ReplyEntity;
import xyz.heetaeb.Woute.domain.reply.entity.ReplyLikeEntity;

@Repository
public interface ReplyLIkeRepository extends JpaRepository<ReplyLikeEntity, Long> {
	
    List<ReplyLikeEntity> findByUserId(Long userId);
    Optional<ReplyLikeEntity> findByReplyIdAndUserId(Long replyId, Long userId);

	

}
