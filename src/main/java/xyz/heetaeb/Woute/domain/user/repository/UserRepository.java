package xyz.heetaeb.Woute.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import xyz.heetaeb.Woute.domain.user.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

	UserEntity findByEmail(String email);
	Optional<UserEntity> findByEmailIgnoreCase(String email);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.email = :email")
	boolean existsByEmail(@Param("email") String email);
	
	
	void deleteById(Long userId);
	
	UserEntity findByNickname(String nickname);
	
	List<UserEntity> findByNicknameContaining(String nickname);
}
