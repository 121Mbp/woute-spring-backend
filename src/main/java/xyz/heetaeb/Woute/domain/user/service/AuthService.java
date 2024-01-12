package xyz.heetaeb.Woute.domain.user.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.heetaeb.Woute.domain.chat.entity.JoinRoom;
import xyz.heetaeb.Woute.domain.chat.repository.JoinRoomRepository;
import xyz.heetaeb.Woute.domain.feed.dto.response.FeedResponse;
import xyz.heetaeb.Woute.domain.feed.entity.AttachEntity;
import xyz.heetaeb.Woute.domain.feed.service.FeedService;
import xyz.heetaeb.Woute.domain.follow.dto.UserResponseDTO;
import xyz.heetaeb.Woute.domain.follow.repository.FollowRepository;
import xyz.heetaeb.Woute.domain.user.dto.TokenDto;
import xyz.heetaeb.Woute.domain.user.dto.request.UpdateNicknameRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UpdateProfileRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UserEmailRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UserLog;
import xyz.heetaeb.Woute.domain.user.dto.request.UserProfileRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UserRequest;
import xyz.heetaeb.Woute.domain.user.dto.response.UserResponse;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;
import xyz.heetaeb.Woute.global.config.jwt.TokenProvider;
import xyz.heetaeb.Woute.global.config.jwt.TokenUtils;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {
	private final AmazonS3Client amazonS3Client;
	private final String FOLDER_PATH = "https://woute-bucket.s3.ap-northeast-2.amazonaws.com/";
    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final FollowRepository followRepository;
    private final FeedService feedService;
    private final JoinRoomRepository joinRoomRepository;
    
    
    public List<UserEntity> selectAll(){
    	System.out.println("select test123");
    	return userRepository.findAll();
    }
    
    public boolean isEmailAlreadyExists(String email) {
        System.out.println("파라메터에 들어온 이메일 : " + email);
        boolean exists = userRepository.existsByEmail(email);
        System.out.println("existsByEmail 결과: " + exists);
        return exists;
    }
  
    public UserResponse join(UserRequest requestDto) {
    	System.out.println("asdkfmlasmdfkl");
		
//		if (userRepository.existsByEmail(requestDto.getEmail())) { throw new
//		  RuntimeException("이미 가입되어 있는 유저입니다"); }
//		
       System.out.println("sdfasdf : " + requestDto.toString());
       UserEntity user = requestDto.toUser(passwordEncoder);
       System.out.println("user : " + user);
        return UserResponse.of(userRepository.save(user));
    }

    public TokenDto login(UserLog requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        System.out.println("aa : "+ authenticationToken);
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        System.out.println("토큰:" + authenticationToken);
        System.out.println("authentiation 제발 여기 통과좀 해줘");
        return tokenProvider.generateTokenDto(authentication);
    }
    
    // currentUser를 사용하여 토큰에서 가져온 사용자 정보에 접근할 수 있습니다.
  
    public void getUserInfo(String token) {
        UserEntity currentUser = TokenUtils.getCurrentUserFromToken(token, "yourSecretKey", userRepository);
    }
    
	@Transactional
	public UserResponseDTO getUserFeed(Long id) {
		UserEntity user = userRepository.findById(id).orElseThrow();
		Long followerCount = followRepository.countByFollowerId(id);
		Long followingCount = followRepository.countByFollowingId(id);
		List<FeedResponse> feeds = feedService.userFeeds(id);
		// 로그인 아이디값 가져오기
//		Boolean hasFollowed = followRepository.countByFollowingIdAndFollowerId(Long.valueOf(authentication.getName()), user.getId());
		
		return UserResponseDTO.builder()
				.id(user.getId())
				.nickname(user.getNickname())
				.introduction(user.getIntroduction())
				.ProfileImage(user.getProfileImage())
				.feedsCount(Long.valueOf(feeds.size()))
				.followerCount(followerCount)
				.followingCount(followingCount)
				.hasFollowed(false)
				.feeds(feeds)
				.build();
	}
	 public byte[] downloadImageSystem(String id) throws IOException {
	        String attaches = id;
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

	            String filePath = FOLDER_PATH;
	            byte[] fileBytes = Files.readAllBytes(new File(filePath).toPath());
	            byteArrayOutputStream.write(fileBytes);
	        
	        return byteArrayOutputStream.toByteArray();
	    }
	
	 public UserEntity findById(Long userId) {
	        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 사용자"));
	    }

	public UserResponse updateUserNickname(Long userId, String nickname) {
	    UserEntity currentUser = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("수정할 수 없는 사용자입니다."));
	    System.out.println("id : "+ userId+"nickname : " + nickname);
	    if (!Objects.equals(nickname, currentUser.getNickname())) {
	        currentUser.setNickname(nickname);
	        System.out.println("닉네임 수정 : " + currentUser);
	    }

	    currentUser.setUpdatedAt(ZonedDateTime.now());

	    UserEntity updatedUser = userRepository.save(currentUser);

	    return UserResponse.from(updatedUser);
	}
	//자기소개 수정
	public UserResponse updateUserIntro(Long userId, String introduction) {
		UserEntity currentUser = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("수정할 수 없는 사용자입니다."));
		System.out.println("id : "+ userId+"introduction : " + introduction);
		if (!Objects.equals(introduction, currentUser.getIntroduction())) {
			currentUser.setIntroduction(introduction);
			System.out.println("자기소개 수정 : " + currentUser);
		}
		
		currentUser.setUpdatedAt(ZonedDateTime.now());
		
		UserEntity updatedUser = userRepository.save(currentUser);
		
		return UserResponse.from(updatedUser);
	}
	//비밀번호 수정
	public UserResponse updateUserpw(Long userId, String password) {
		UserEntity currentUser = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("수정할 수 없는 사용자입니다."));
		System.out.println("id : "+ userId+"password : " + password);
		
			currentUser.setPassword(passwordEncoder.encode(password));
		System.out.println("비밀번호 수정 : " + currentUser);
		
		currentUser.setUpdatedAt(ZonedDateTime.now());
		
		UserEntity updatedUser = userRepository.save(currentUser);
		
		return UserResponse.from(updatedUser);
	}
	public UserResponse updateUserpw(String password, String email) {
		UserEntity currentUser = userRepository.findByEmail(email);
		System.out.println("email : "+ email+"password : " + password);
		System.out.println("조회 : " + currentUser);
		
			currentUser.setPassword(passwordEncoder.encode(password));
		System.out.println("비밀번호 수정 : " + currentUser);
		
		currentUser.setUpdatedAt(ZonedDateTime.now());
		
		UserEntity updatedUser = userRepository.save(currentUser);
		
		return UserResponse.from(updatedUser);
	}
	  public String deleteUserById(Long userId) {
	        UserEntity user = userRepository.findById(userId).orElseThrow();
	        if (user != null) {
	        	userRepository.deleteById(userId);
	        	return "Y";
	        }else {
	        	return "N";
	        }
	    }
	
	public boolean passwordCheck(Long userId, String password) {
	    UserEntity currentUser = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("수정할 수 없는 사용자입니다."));
	    System.out.println("화면으로 부터온 pw : " + password);
	    System.out.println("db pw" + currentUser.getPassword());
	    if (passwordEncoder.matches(password, currentUser.getPassword())) {
	        System.out.println("비교 true");
	        return true;
	    } else {
	        System.out.println("비교 false");
	        return false;
	    }
	}
	public boolean findUserByEmail(UserEmailRequest request) {
	   	System.out.println("클라이언트에서 받은 email : " + request.getEmail());
        UserEntity user =  userRepository.findByEmail(request.getEmail());
        System.out.println("서버 email 조회 : " + user);
        if (user != null) {
        	return true;
        }else {
        	return false;
        }
    }
	//uuid 가져오기
	public String getUUID(Long id) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("수정할 수 없는 사용자입니다."));
		System.out.println("로그인 되어있는 사용자의 아이디 조회 : "+ id);
		String uuid = user.getProfileImage();
		System.out.println("uuid : "+uuid);
		return uuid;
	}
	
	//파일첨부
	 // 이미지 업로드 및 파일 이름 반환
	@Value("${cloud.aws.s3.bucket}")
    private String bucket;
	
	public String uploadProfileImage(MultipartFile file,Long id) throws IOException {
	    try {
	    	System.out.println("file : "+ file);
	        // 업로드된 파일의 이름을 생성
	        String fileName = generateUniqueFileName(file.getOriginalFilename());
	        System.out.println("fileName : "+ fileName);
	        // 파일을 서버의 업로드 디렉토리에 저장
	        ObjectMetadata metadata = new ObjectMetadata();
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
//	        Path filePath = Path.of(FOLDER_PATH, fileName);
	        
//	        System.out.println("filePath : "+ filePath);
//	        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	        UserEntity userEntity = userRepository.findById(id).orElseThrow();
//	        String PathString = filePath.toString();
            // 업로드된 파일 이름을 엔터티의 profileImage 필드에 설정
            userEntity.setProfileImage(fileName);
            System.out.println("뭐가들어있어 :" + userEntity);
            UserEntity updateUser = userRepository.save(userEntity);
	        // 저장된 파일의 이름을 반환
            
	        return fileName;
	    } catch (Exception e) {
	        // 예외 발생 시 로그 출력
	        e.printStackTrace();
	        throw e; // 예외를 다시 던져서 컨트롤러에서 500 에러 응답이 발생하도록 함
	    }
	}

    // 고유한 파일 이름 생성
    private String generateUniqueFileName(String originalFileName) {
        // 파일 확장자 추출
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        
        // UUID를 이용하여 고유한 파일 이름 생성
        return UUID.randomUUID().toString() + extension;
    }
    
    @Transactional
    public UserResponseDTO getUserFeed(Long id, Long myId) {
        UserEntity user = userRepository.findById(id).orElseThrow();
        Long followerCount = followRepository.countByFollowerId(id);
        Long followingCount = followRepository.countByFollowingId(id);
        List<FeedResponse> feeds = feedService.userFeeds(id);
        JoinRoom room = joinRoomRepository.findByMyUserIdAndToUserId(myId, id);
        // 로그인 아이디값 가져오기
        Long hasFollowed = followRepository.countByFollowingIdAndFollowerId(myId, id);
        if(room != null) {
        	return UserResponseDTO.builder()
        			.id(user.getId())
        			.roomId(room.getRoomId())
        			.nickname(user.getNickname())
        			.introduction(user.getIntroduction())
        			.ProfileImage(user.getProfileImage())
        			.feedsCount(Long.valueOf(feeds.size()))
        			.followerCount(followerCount)
        			.followingCount(followingCount)
        			.hasFollowed(hasFollowed == 1 ? true : false)
        			.feeds(feeds)
        			.build();
        } else {
        	return UserResponseDTO.builder()
        			.id(user.getId())
        			.nickname(user.getNickname())
        			.introduction(user.getIntroduction())
        			.ProfileImage(user.getProfileImage())
        			.feedsCount(Long.valueOf(feeds.size()))
        			.followerCount(followerCount)
        			.followingCount(followingCount)
        			.hasFollowed(hasFollowed == 1 ? true : false)
        			.feeds(feeds)
        			.build();
        	
        }
    }
    
    @Transactional
    public UserResponseDTO getMyFeed(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow();
        Long followerCount = followRepository.countByFollowerId(id);
        Long followingCount = followRepository.countByFollowingId(id);
        List<FeedResponse> feeds = feedService.userFeeds(id);
        
        return UserResponseDTO.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .introduction(user.getIntroduction())
                .ProfileImage(user.getProfileImage())
                .feedsCount(Long.valueOf(feeds.size()))
                .followerCount(followerCount)
                .followingCount(followingCount)
                .feeds(feeds)
                .build();
    }
   
}
