package xyz.heetaeb.Woute.domain.follow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.heetaeb.Woute.domain.follow.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByNickName(String nickname);
	
}
