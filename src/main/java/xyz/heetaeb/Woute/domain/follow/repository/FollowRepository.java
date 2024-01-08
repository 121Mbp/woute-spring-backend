package xyz.heetaeb.Woute.domain.follow.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import xyz.heetaeb.Woute.domain.follow.entity.Follow;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findByFollowingIdAndFollowerId(Long followingId, Long followerId);
    
    Long countByFollowerId(Long FollowerId);
    
    Long countByFollowingId(Long FollowingId);
    
    Long countByFollowingIdAndFollowerId(Long followingId, Long FollowerId);
    
    @Query(value = "SELECT *  FROM (SELECT ROWNUM RNUM, TABLE_1.*  FROM \r\n"
    		+ "(select\r\n"
    		+ "        f1_0.id,\r\n"
    		+ "        f1_0.created_at,\r\n"
    		+ "        f1_0.follow_state,\r\n"
    		+ "        f1_0.follower_id,\r\n"
    		+ "        f1_0.following_id \r\n"
    		+ "    from\r\n"
    		+ "        w_follows f1_0 \r\n"
    		+ "    left join\r\n"
    		+ "        w_user f2_0 \r\n"
    		+ "	on f2_0.id=f1_0.following_id \r\n"
    		+ "	WHERE f2_0.id =:id \r\n"
    		+ "	order BY f1_0.created_at DESC\r\n"
    		+ ") TABLE_1) \r\n"
    		+ "WHERE RNUM BETWEEN 0 AND 10"
    		, nativeQuery = true)
    List<Follow> findByFollowingId(@Param("id") Long followingId);
    
    @Query(value = "SELECT *  FROM (SELECT ROWNUM RNUM, TABLE_1.*  FROM \r\n"
    		+ "(select\r\n"
    		+ "        f1_0.id,\r\n"
    		+ "        f1_0.created_at,\r\n"
    		+ "        f1_0.follow_state,\r\n"
    		+ "        f1_0.follower_id,\r\n"
    		+ "        f1_0.following_id \r\n"
    		+ "    from\r\n"
    		+ "        w_follows f1_0 \r\n"
    		+ "    left join\r\n"
    		+ "        w_user f2_0 \r\n"
    		+ "	on f2_0.id=f1_0.follower_id \r\n"
    		+ "	WHERE f2_0.id =:id \r\n"
    		+ "	order BY f1_0.created_at DESC\r\n"
    		+ ") TABLE_1) \r\n"
    		+ "WHERE RNUM BETWEEN 0 AND 10"
    		, nativeQuery = true)
    List<Follow> findByFollowerId(@Param("id") Long followerId);
}
