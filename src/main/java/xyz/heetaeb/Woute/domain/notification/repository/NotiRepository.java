package xyz.heetaeb.Woute.domain.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.heetaeb.Woute.domain.notification.entity.Notification;

public interface NotiRepository extends JpaRepository<Notification, Long>{
	List<Notification> findByUserIdOrderByCreatedAtDesc(Long id);
	
	List<Notification> findByUserIdAndRead(Long id, Boolean read);
	
	Long countByUserIdAndRead(Long id, Boolean read);
}
