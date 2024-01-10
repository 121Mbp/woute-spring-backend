package xyz.heetaeb.Woute.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.heetaeb.Woute.domain.feed.entity.AttachEntity;
import xyz.heetaeb.Woute.domain.feed.entity.TagsEntity;

import java.util.List;

@Repository
public interface TagsRepository extends JpaRepository<TagsEntity, Long> {
    public List<TagsEntity> findAllByFeedId(Long feeId);
    List<TagsEntity> findDistinctByWordsContaining(String keyword);
}
