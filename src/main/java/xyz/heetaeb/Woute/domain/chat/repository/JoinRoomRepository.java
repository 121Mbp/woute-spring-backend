package xyz.heetaeb.Woute.domain.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.heetaeb.Woute.domain.chat.entity.JoinRoom;

public interface JoinRoomRepository extends JpaRepository<JoinRoom, Long> {
	List<JoinRoom> findByMyUserId(Long userid);
	
	JoinRoom findByRoomIdAndMyUserId(Long roomid, Long userid);
	
	Long countByRoomId(Long roomid);
	
}
