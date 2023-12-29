package xyz.heetaeb.Woute.domain.feed.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.heetaeb.Woute.domain.feed.dto.request.FeedRequest;
import xyz.heetaeb.Woute.domain.feed.dto.request.LikeRequest;
import xyz.heetaeb.Woute.domain.feed.dto.response.CourseResponse;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse;
import xyz.heetaeb.Woute.domain.feed.entity.AttachEntity;
import xyz.heetaeb.Woute.domain.feed.entity.CourseEntity;
import xyz.heetaeb.Woute.domain.feed.entity.FeedEntity;
import xyz.heetaeb.Woute.domain.feed.entity.LikeEntity;
import xyz.heetaeb.Woute.domain.feed.repository.AttachRepository;
import xyz.heetaeb.Woute.domain.feed.repository.CourseRepository;
import xyz.heetaeb.Woute.domain.feed.repository.FeedRepository;
import xyz.heetaeb.Woute.domain.feed.repository.LikeRepository;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final CourseRepository courseRepository;
    private final AttachRepository attachRepository;
    private final LikeRepository likeRepository;

    // 피드 리스트
    public List<FeedResponse> feedList() {
        List<FeedEntity> feeds = feedRepository.findAll();
        return feeds.stream().map(post -> {
            Long feedId = post.getId();
            List<AttachEntity> attaches = attachRepository.findAllByFeedId(feedId);
            return FeedResponse.builder()
                    .id(post.getId())
                    .nickname(post.getNickname())
                    .profileImage(post.getProfileImage())
                    .type(post.getType())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .hashtag(post.getHashtag())
                    .heartCount(post.getHeartCount())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .attaches(
                            attaches.stream().map(attach -> FeedResponse.Attach.builder()
                                    .uuid(attach.getUuid())
                                    .origin(attach.getOrigin())
                                    .build()).toList()
                    )
                    .build();
        }).toList();
    }

    //상세 페이지
    public CourseResponse courseList(Long feedId) {
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();
        List<CourseEntity> courses = courseRepository.findAllByFeedId(feedId);
        List<AttachEntity> attaches = attachRepository.findAllByFeedId(feedId);
        return CourseResponse.builder()
                .id(feed.getId())
                .nickname(feed.getNickname())
                .profileImage(feed.getProfileImage())
                .type(feed.getType())
                .title(feed.getTitle())
                .content(feed.getContent())
                .hashtag(feed.getHashtag())
                .heartCount(feed.getHeartCount())
                .createdAt(feed.getCreatedAt())
                .updatedAt(feed.getUpdatedAt())
                .courses(
                        courses.stream().map(places -> CourseResponse.Course.builder()
                                .id(places.getId())
                                .store(places.getStore())
                                .address(places.getAddress())
                                .phone(places.getPhone())
                                .homepage(places.getHomepage())
                                .category(places.getCategory())
                                .latitude(places.getLatitude())
                                .longitude(places.getLongitude())
                                .build()
                        ).toList()
                )
                .attaches(
                        attaches.stream().map(images -> CourseResponse.Attach.builder()
                                .uuid(images.getUuid())
                                .origin(images.getOrigin())
                                .build()
                        ).toList()
                ).build();
    }

    // 피드 등록
    @Transactional
    public void insertFeed(FeedRequest request) {
        FeedEntity feed = FeedEntity.builder()
                .userId(request.getUserId())
                .nickname(request.getNickname())
                .profileImage(request.getProfileImage())
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .hashtag(request.getHashtag())
                .heartCount(request.getHeartCount())
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();
        feedRepository.save(feed);
        request.getCourse().forEach(places -> {
            CourseEntity course = CourseEntity.builder()
                    .feedId(feed.getId())
                    .store(places.getStore())
                    .address(places.getAddress())
                    .phone(places.getPhone())
                    .homepage(places.getHomepage())
                    .category(places.getCategory())
                    .latitude(places.getLatitude())
                    .longitude(places.getLongitude())
                    .build();
            courseRepository.save(course);
        });
        request.getAttach().forEach(images -> {
            AttachEntity attach = AttachEntity.builder()
                    .feedId(feed.getId())
                    .origin(images.getOrigin())
                    .build();
            attachRepository.save(attach);
        });
    }

    @Transactional
    public void modifyFeed(Long feedId, FeedRequest request) {
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();
        feed.changeFeedAndCourse(
                request.getTitle(),
                request.getContent(),
                request.getHashtag()
        );
        feedRepository.save(feed);

        List<CourseEntity> course = courseRepository.findAllByFeedId(feedId);
        courseRepository.deleteAll(course);
        List<AttachEntity> attach = attachRepository.findAllByFeedId(feedId);
        attachRepository.deleteAll(attach);

        request.getCourse().forEach(places -> {
            CourseEntity courses = CourseEntity.builder()
                    .feedId(feed.getId())
                    .store(places.getStore())
                    .address(places.getAddress())
                    .phone(places.getPhone())
                    .homepage(places.getHomepage())
                    .category(places.getCategory())
                    .latitude(places.getLatitude())
                    .longitude(places.getLongitude())
                    .build();
            courseRepository.save(courses);
        });

        request.getAttach().forEach(images -> {
            AttachEntity attaches = AttachEntity.builder()
                    .feedId(feed.getId())
                    .origin(images.getOrigin())
                    .build();
            attachRepository.save(attaches);
        });
    }

    @Transactional
    public void deleteFeed(Long feedId) {
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();
        feedRepository.delete(feed);
        List<CourseEntity> courses = courseRepository.findAllByFeedId(feedId);
        courseRepository.deleteAll(courses);
        List<AttachEntity> attaches = attachRepository.findAllByFeedId(feedId);
        attachRepository.deleteAll(attaches);
    }

    @Transactional
    public void likeFeed(Long feedId, LikeRequest request) {
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();
        int increase = feed.getHeartCount() + 1;
        feed.changeFeedLike(increase);
        feedRepository.save(feed);

        LikeEntity like = LikeEntity.builder()
                .nickname(request.getNickname())
                .profileImage(request.getProfileImage())
                .createdAt(ZonedDateTime.now())
                .build();
        likeRepository.save(like);
    }

    @Transactional
    public void unlikeFeed(Long feedId, Long likeId, LikeRequest request) {
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();
        int decrease = feed.getHeartCount() - 1;
        feed.changeFeedLike(decrease);
        feedRepository.save(feed);
        LikeEntity unlike = likeRepository.findById(likeId).orElseThrow();
        likeRepository.delete(unlike);
    }
}
