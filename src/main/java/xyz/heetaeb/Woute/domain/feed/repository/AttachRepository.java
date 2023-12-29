package xyz.heetaeb.Woute.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.heetaeb.Woute.domain.feed.entity.AttachEntity;

import java.util.List;

@Repository
public interface AttachRepository extends JpaRepository<AttachEntity, String> {
    public List<AttachEntity> findAllByFeedId(Long feeId);
}
