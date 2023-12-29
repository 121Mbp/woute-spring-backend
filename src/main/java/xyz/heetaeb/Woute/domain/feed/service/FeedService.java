package xyz.heetaeb.Woute.domain.feed.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.heetaeb.Woute.domain.feed.dto.request.CourseRequest;
import xyz.heetaeb.Woute.domain.feed.dto.request.FeedRequest;
import xyz.heetaeb.Woute.domain.feed.dto.request.LikeRequest;
import xyz.heetaeb.Woute.domain.feed.dto.request.TagsRequest;
import xyz.heetaeb.Woute.domain.feed.dto.response.CourseResponse;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse;
import xyz.heetaeb.Woute.domain.feed.dto.response.TagsResponse;
import xyz.heetaeb.Woute.domain.feed.entity.*;
import xyz.heetaeb.Woute.domain.feed.repository.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final String FOLDER_PATH = "/Users/kevin/Desktop/image/";
    private final FeedRepository feedRepository;
    private final CourseRepository courseRepository;
    private final AttachRepository attachRepository;
    private final LikeRepository likeRepository;
    private final TagsRepository tagsRepository;

    // 피드 리스트
    public List<FeedResponse> feedList() {
        List<FeedEntity> feeds = feedRepository.findAll();
        return feeds.stream().map(post -> {
            Long feedId = post.getId();
            List<AttachEntity> attaches = attachRepository.findAllByFeedId(feedId);
            List<TagsEntity> hashtags = tagsRepository.findAllByFeedId(feedId);
            return FeedResponse.builder()
                    .id(post.getId())
                    .nickname(post.getNickname())
                    .profileImage(post.getProfileImage())
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
                    .build();
        }).toList();
    }

    //상세 페이지
    public CourseResponse courseList(Long feedId) {
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();
        List<TagsEntity> hashtags = tagsRepository.findAllByFeedId(feedId);
        List<CourseEntity> courses = courseRepository.findAllByFeedId(feedId);
        List<AttachEntity> attaches = attachRepository.findAllByFeedId(feedId);

        return CourseResponse.builder()
                .id(feed.getId())
                .nickname(feed.getNickname())
                .profileImage(feed.getProfileImage())
                .type(feed.getType())
                .title(feed.getTitle())
                .content(feed.getContent())
                .heartCount(feed.getHeartCount())
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
        System.out.println(requestFeed.getTitle());
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
                file.transferTo(new File(filePath));
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

    @Transactional
    public void modifyFeed(Long feedId, FeedRequest request) {
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();
        feed.changeFeedAndCourse(
                request.getTitle(),
                request.getContent()
        );
        feedRepository.save(feed);

        List<CourseEntity> course = courseRepository.findAllByFeedId(feedId);
        courseRepository.deleteAll(course);
        List<AttachEntity> attach = attachRepository.findAllByFeedId(feedId);
        attachRepository.deleteAll(attach);

        request.getCourse().forEach(places -> {
            CourseEntity courses = CourseEntity.builder()
                    .feedId(feed.getId())
                    .code(places.getCode())
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
