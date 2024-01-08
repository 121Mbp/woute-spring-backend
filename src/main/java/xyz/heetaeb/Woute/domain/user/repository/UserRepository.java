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
//	Optional<UserEntity> findByEmail(String email);
	UserEntity findByEmail(String email);
	Optional<UserEntity> findByEmailIgnoreCase(String email);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.email = :email")
	boolean existsByEmail(@Param("email") String email);
//	List<UserEntity> findAll();
//	@Query(value = "SELECT COUNT(u) FROM UserEntity u WHERE u.email = :email")
//	Long countByEmail(@Param("email") String email);
//	default boolean existsByEmail(String email) {
//		System.out.println(findByEmail(email));
//		System.out.println("email : "+ email+"countBy : "+countByEmail(email));
//	    return countByEmail(email) > 0;
//	}
	

	
	UserEntity findByNickname(String nickname);
}
