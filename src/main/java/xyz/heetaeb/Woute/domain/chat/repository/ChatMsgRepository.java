package xyz.heetaeb.Woute.domain.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.heetaeb.Woute.domain.chat.entity.ChatMsg;

public interface ChatMsgRepository extends JpaRepository<ChatMsg, Long> {
	List<ChatMsg> findByRoomId(Long roomId);
	
	List<ChatMsg> findByRoomIdOrderByCreatedAtDesc(Long roomId);
}
