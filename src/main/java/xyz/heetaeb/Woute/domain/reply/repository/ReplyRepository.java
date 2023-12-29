package xyz.heetaeb.Woute.domain.reply.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.heetaeb.Woute.domain.reply.entity.ReplyEntity;


public interface ReplyRepository extends JpaRepository<ReplyEntity, Long>{
	
	public List<ReplyEntity> findByFeedId(Long feedId);
	
	Optional<ReplyEntity> findByFeedIdAndId(Long feedId, Long id);
	
}
