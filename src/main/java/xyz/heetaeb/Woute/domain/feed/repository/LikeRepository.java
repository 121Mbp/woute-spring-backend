package xyz.heetaeb.Woute.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.heetaeb.Woute.domain.feed.entity.LikeEntity;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

}
