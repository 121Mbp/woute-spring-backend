package xyz.heetaeb.Woute.domain.feed.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.heetaeb.Woute.domain.feed.entity.FeedEntity;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, Long> {
    public List<FeedEntity> findByUserIdAndType(Long userId, String type);
    public List<FeedEntity> findByType(String type);
    public List<FeedEntity> findByUserId(Long userId);
//    public List<FeedEntity> findAll();
}
