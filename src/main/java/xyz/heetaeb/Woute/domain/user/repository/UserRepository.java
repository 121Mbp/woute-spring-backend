package xyz.heetaeb.Woute.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.heetaeb.Woute.domain.user.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
	Optional<UserEntity> findByEmail(String email);
	boolean existsByEmail(String email);
	
	UserEntity findByNickname(String nickname);
}
