package xyz.heetaeb.Woute.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.heetaeb.Woute.domain.feed.entity.FeedEntity;

@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, Long> {

}
