package xyz.heetaeb.Woute.domain.feed.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.heetaeb.Woute.domain.feed.dto.request.CourseRequest;
import xyz.heetaeb.Woute.domain.feed.dto.request.FeedRequest;
import xyz.heetaeb.Woute.domain.feed.dto.request.LikeRequest;
import xyz.heetaeb.Woute.domain.feed.dto.request.TagsRequest;
import xyz.heetaeb.Woute.domain.feed.dto.response.CourseResponse;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse;
import xyz.heetaeb.Woute.domain.feed.entity.*;
import xyz.heetaeb.Woute.domain.feed.repository.*;
import xyz.heetaeb.Woute.domain.notification.service.NotiService;
import xyz.heetaeb.Woute.domain.reply.entity.ReplyEntity;
import xyz.heetaeb.Woute.domain.reply.repository.ReplyRepository;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Component
public class FeedService {
    private final String FOLDER_PATH = "/Users/kevin/Desktop/image/";
    private final FeedRepository feedRepository;
    private final CourseRepository courseRepository;
    private final AttachRepository attachRepository;
    private final LikeRepository likeRepository;
    private final TagsRepository tagsRepository;
    private final ReplyRepository replyRepository;
    private final NotiService notiService;
    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 피드 리스트
    public List<FeedResponse> feedList() {
        List<FeedEntity> feeds = feedRepository.findAll(Sort.by("createdAt").descending());
        return dataList(feeds);
    }

    // 코스 / 피드 리스트
    public List<FeedResponse> typeFeedList(String type) {
        List<FeedEntity> feeds = feedRepository.findByType(type);
        return dataList(feeds);
    }

    // 유저 피드 리스트
    public List<FeedResponse> userFeedList(Long userId) {
        List<FeedEntity> feeds = feedRepository.findByUserIdAndType(userId, "feeds");
        return dataList(feeds);
    }

    // 유저 코스 리스트
    public List<FeedResponse> userCourseFeedList(Long userId) {
        List<FeedEntity> feeds = feedRepository.findByUserIdAndType(userId, "courses");
        return dataList(feeds);
    }

    // 유저 좋아요 리스트
    public List<FeedResponse> getAllUserFeedsLike(Long userId) {
        List<LikeEntity> likes = likeRepository.findAllByUserId(userId);
        List<FeedEntity> feeds = likes.stream()
                .map(like -> feedRepository.findById(like.getFeedId()).orElse(null))
                .collect(Collectors.toList());

        return dataList(feeds);
    }

    // 유저 피드 리스트
    public List<FeedResponse> userFeeds(Long userId) {
        List<FeedEntity> feeds = feedRepository.findByUserId(userId);
        return dataList(feeds);
    }

    public List<FeedResponse> dataList(List<FeedEntity> feeds) {
        return feeds.stream().map(post -> {
            Long feedId = post.getId();
            List<AttachEntity> attaches = attachRepository.findAllByFeedId(feedId);
            List<TagsEntity> hashtags = tagsRepository.findAllByFeedId(feedId);
            List<LikeEntity> likes = likeRepository.findAllByFeedId(feedId);
            List<ReplyEntity> reply = replyRepository.findByFeedId(feedId);
            Optional<UserEntity> user = userRepository.findById(post.getUserId());
            int replyCount = reply.size();
            return FeedResponse.builder()
                    .id(post.getId())
                    .userId(post.getUserId())
                    .nickname(user.map(UserEntity::getNickname).orElse(null))
                    .profileImage(user.map(UserEntity::getProfileImage).orElse(null))
                    .type(post.getType())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .heartCount(post.getHeartCount())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .tags(
                            hashtags.stream().map(tag -> FeedResponse.Tag.builder()
                                    .id(tag.getId())
                                    .words(tag.getWords())
                                    .build()).toList()
                    )
                    .attaches(
                            attaches.stream().map(attach -> FeedResponse.Attach.builder()
                                    .uuid(attach.getUuid())
                                    .origin(attach.getOrigin())
                                    .type(attach.getType())
                                    .filePath(attach.getFilePath())
                                    .build()).toList()
                    )
                    .likes(
                            likes.stream().map(like -> {
                                Optional<UserEntity> userLike = userRepository.findById(like.getUserId());

                                return FeedResponse.Like.builder()
                                        .id(like.getId())
                                        .userId(like.getUserId())
                                        .nickname(userLike.map(UserEntity::getNickname).orElse(null))
                                        .profileImage(userLike.map(UserEntity::getProfileImage).orElse(null))
                                        .build();
                            }).toList()
                    )
                    .replyCount(replyCount)
                    .build();
        }).toList();
    }

    //상세 페이지
    public CourseResponse courseList(Long feedId) {
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();
        List<TagsEntity> hashtags = tagsRepository.findAllByFeedId(feedId);
        List<CourseEntity> courses = courseRepository.findAllByFeedId(feedId);
        List<AttachEntity> attaches = attachRepository.findAllByFeedId(feedId);
        List<LikeEntity> likes = likeRepository.findAllByFeedId(feedId);
        Optional<UserEntity> user = userRepository.findById(feed.getUserId());
        return CourseResponse.builder()
                .id(feed.getId())
                .userId(feed.getUserId())
                .nickname(user.map(UserEntity::getNickname).orElse(null))
                .profileImage(user.map(UserEntity::getProfileImage).orElse(null))
                .type(feed.getType())
                .title(feed.getTitle())
                .content(feed.getContent())
                .createdAt(feed.getCreatedAt())
                .updatedAt(feed.getUpdatedAt())
                .tags(
                        hashtags.stream().map(tag -> CourseResponse.Tag.builder()
                                .id(tag.getId())
                                .words(tag.getWords())
                                .build()
                        ).toList()
                )
                .courses(
                        courses.stream().map(places -> CourseResponse.Course.builder()
                                .id(places.getId())
                                .code(places.getCode())
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
                .likes(
                        likes.stream().map(like -> {
                            Optional<UserEntity> userLike = userRepository.findById(like.getUserId());

                            return CourseResponse.Like.builder()
                                    .id(like.getId())
                                    .userId(like.getUserId())
                                    .nickname(userLike.map(UserEntity::getNickname).orElse(null))
                                    .profileImage(userLike.map(UserEntity::getProfileImage).orElse(null))
                                    .build();
                        }).toList()
                )
                .attaches(
                        attaches.stream().map(images -> CourseResponse.Attach.builder()
                                .uuid(images.getUuid())
                                .origin(images.getOrigin())
                                .type(images.getType())
                                .filePath(images.getFilePath())
                                .build()
                        ).toList()
                ).build();
    }

    // 첨부 파일
    public byte[] downloadImageSystem(String id) throws IOException {
        List<AttachEntity> attaches = attachRepository.findByUuid(id);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        for (AttachEntity attach : attaches) {
            String filePath = attach.getFilePath();
            byte[] fileBytes = Files.readAllBytes(new File(filePath).toPath());
            byteArrayOutputStream.write(fileBytes);
        }
        return byteArrayOutputStream.toByteArray();
    }

    //피드 등록
    @Transactional
    public void insertFeed(
            FeedRequest requestFeed,
            List<CourseRequest> requestCourses,
            List<TagsRequest> requestTags,
            List<MultipartFile> flies
    ) {
        FeedEntity feed = FeedEntity.builder()
                .userId(requestFeed.getUserId())
                .nickname(requestFeed.getNickname())
                .profileImage(requestFeed.getProfileImage())
                .type(requestFeed.getType())
                .title(requestFeed.getTitle())
                .content(requestFeed.getContent())
                .heartCount(requestFeed.getHeartCount())
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();
        feedRepository.save(feed);
        requestCourses.forEach(course -> {
            try {
                CourseEntity courseEntity = courseRepository.save(
                        CourseEntity.builder()
                                .feedId(feed.getId())
                                .code(course.getCode())
                                .store(course.getStore())
                                .address(course.getAddress())
                                .phone(course.getPhone())
                                .homepage(course.getHomepage())
                                .category(course.getCategory())
                                .latitude(course.getLatitude())
                                .longitude(course.getLongitude())
                                .build()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        requestTags.forEach(tag -> {
            try {
                TagsEntity tagsEntity = tagsRepository.save(
                        TagsEntity.builder()
                                .feedId(feed.getId())
                                .words(tag.getWords())
                                .build()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        List<String> attaches = flies.stream().map(file -> {
            try {
                String uuid = UUID.randomUUID().toString();
                String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String cdn = uuid + ext;
                String filePath = FOLDER_PATH + cdn;
                attachRepository.save(
                        AttachEntity.builder()
                                .uuid(cdn)
                                .feedId(feed.getId())
                                .origin(file.getOriginalFilename())
                                .type(file.getContentType())
                                .filePath(filePath)
                                .build()
                );
                ObjectMetadata metadata = new ObjectMetadata();
                amazonS3Client.putObject(new PutObjectRequest(bucket, cdn, file.getInputStream(), metadata));
//                file.transferTo(new File(filePath));
                if(filePath != null) {
                    return "파일 업로드 성공" + filePath;
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    // 피드 수정
    @Transactional
    public void modifyFeed(
            Long feedId, FeedRequest requestFeed,
            List<TagsRequest> requestTags
    ) {
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();
        feed.changeFeedAndCourse(
                requestFeed.getTitle(),
                requestFeed.getContent()
        );
        feedRepository.save(feed);

        List<TagsEntity> tags = tagsRepository.findAllByFeedId(feedId);
        tagsRepository.deleteAll(tags);

        requestTags.forEach(tag -> {
            try {
                TagsEntity tagsEntity = tagsRepository.save(
                        TagsEntity.builder()
                                .feedId(feed.getId())
                                .words(tag.getWords())
                                .build()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // 피드 삭제
    @Transactional
    public void deleteFeed(Long feedId) {
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();
        feedRepository.delete(feed);
        List<CourseEntity> courses = courseRepository.findAllByFeedId(feedId);
        courseRepository.deleteAll(courses);
        List<TagsEntity> tags = tagsRepository.findAllByFeedId(feedId);
        tagsRepository.deleteAll(tags);
        List<AttachEntity> attaches = attachRepository.findAllByFeedId(feedId);
        attaches.forEach(attach -> {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, attach.getUuid()));
        });
        attachRepository.deleteAll(attaches);
        List<ReplyEntity> reply = replyRepository.findByFeedId(feedId);
        replyRepository.deleteAll(reply);
    }

    // 좋아요
    @Transactional
    public void likeFeed(Long feedId, LikeRequest request) {
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();
        Optional<UserEntity> user = userRepository.findById(request.getUserId());
        int increase = feed.getHeartCount() + 1;
        feed.changeFeedLike(increase);
        feedRepository.save(feed);
        
        // 게시글 좋아요 알림(자신 제외)
        if(feed.getUserId() != request.getUserId()) {
        	notiService.send(feed.getUserId(),
        			request.getUserId(),
        			"님이 게시글에 좋아요를 눌렀습니다.",
        			"/p/" + feed.getId(),
        			feed.getType());
        }
        LikeEntity like = LikeEntity.builder()
                .feedId(feed.getId())
                .userId(request.getUserId())
                .nickname(user.map(UserEntity::getNickname).orElse(null))
                .profileImage(user.map(UserEntity::getProfileImage).orElse(null))
                .createdAt(ZonedDateTime.now())
                .build();
        likeRepository.save(like);
    }

    // 좋아요 취소
    @Transactional
    public void unlikeFeed(Long feedId, Long likeId) {
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();
        int decrease = feed.getHeartCount() - 1;
        feed.changeFeedLike(decrease);
        feedRepository.save(feed);
        LikeEntity unlike = likeRepository.findById(likeId).orElseThrow();
        likeRepository.delete(unlike);
    }
}
