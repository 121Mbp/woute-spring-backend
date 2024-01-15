package xyz.heetaeb.Woute.domain.user.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.core.io.Resource;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.heetaeb.Woute.domain.chat.dto.ChatRequestDTO;
import xyz.heetaeb.Woute.domain.follow.dto.UserResponseDTO;
import xyz.heetaeb.Woute.domain.user.dto.TokenDto;
import xyz.heetaeb.Woute.domain.user.dto.request.FindPwRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UpdateIntroRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UpdateNicknameRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UpdateProfileRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UpdatePwRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UserEmailRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UserLog;
import xyz.heetaeb.Woute.domain.user.dto.request.UserProfileRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UserRequest;
import xyz.heetaeb.Woute.domain.user.dto.response.UserResponse;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;
import xyz.heetaeb.Woute.domain.user.service.AuthService;
//import xyz.heetaeb.Woute.domain.user.service.EmailService;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthController {
	private final String FOLDER_PATH = "https://woute-bucket.s3.ap-northeast-2.amazonaws.com/";
	@PersistenceContext
	private EntityManager entityManager;
    private final AuthService authService;
//    private final EmailService emailService;
	@Operation(summary = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<UserResponse> join(@RequestBody UserRequest requestDto) {
		
		System.out.println("데이터확인1");
		System.out.println("데이터확인2 :" + requestDto);
        return ResponseEntity.ok(authService.join(requestDto));
    }
	@Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserLog requestDto) {
		System.out.println("aabbbb");
		System.out.println("aaaa : "+requestDto.toString());
        return ResponseEntity.ok(authService.login(requestDto));
    }
	@Operation(summary = "프로필 닉네임 수정")
	@PutMapping("/modifyprofile/nickname/{id}")
	public ResponseEntity<UserResponse> updateProfile(
	        @PathVariable("id") Long id,
	        @RequestBody UpdateNicknameRequest updateRequest) {
	    return ResponseEntity.ok(authService.updateUserNickname(id, updateRequest.getNickname()));
	}
	
	@Operation(summary = "프로필 자기소개 수정")
	@PutMapping("/modifyprofile/introduction/{id}")
	public ResponseEntity<UserResponse> updateProfile(
			@PathVariable("id") Long id,
			@RequestBody UpdateIntroRequest updateRequest) {
		return ResponseEntity.ok(authService.updateUserIntro(id, updateRequest.getIntroduction()));
	}
	  @Operation(summary="비밀번호변경")
	   @PutMapping("/modifyprofile/changePw/{id}")
	   public ResponseEntity<UserResponse> changePw(@PathVariable("id")Long id,@RequestBody UpdatePwRequest request) {
		   return ResponseEntity.ok(authService.updateUserpw(id, request.getPassword()));
	   }
	  @Operation(summary="비밀번호찾기의 변경")
	   @PutMapping("/login/changePw")
	   public ResponseEntity<UserResponse> changePw(@RequestBody FindPwRequest request) {
		   return ResponseEntity.ok(authService.updateUserpw(request.getPassword(), request.getEmail()));
	   }
 
   @Operation(summary = "토큰 유저정보")
   @PostMapping("/userinfosave/{id}")
   public ResponseEntity<UserEntity> userInfoSave(@RequestParam("id") Long id) {
       System.out.println("토큰에 있는 id: " + id);
       System.out.println(authService.findById(id));
       return ResponseEntity.ok(authService.findById(id));
   }
   @Operation(summary = "비밀번호 확인")
   @PostMapping("/modifyprofile/checkPw/{id}")
   public String checkPw(@PathVariable("id")Long id, @RequestBody UpdatePwRequest request) {
	   log.info("Received request for password check. User ID: {}", id);
	   	boolean mathchPw = authService.passwordCheck(id, request.getPassword());
	   if(mathchPw) {
		   return "Y";
	   } else {
		   return "N";
	   }
   }
   @Operation(summary="유저삭제")
   @DeleteMapping("/modifyprofile/deleteUser/{id}")
   public String deleteUser(@PathVariable("id")Long id) {
	  return authService.deleteUserById(id);
   }
   @Operation(summary = "프로필 uuid가져오기")
   @PostMapping("/users/uuid/{id}")
   public UserProfileRequest getUUID(@PathVariable("id")Long id,@RequestBody UserProfileRequest request) {
	   System.out.println("접속되어있는 유저의 id : "+id);
	   request.setProfileImage(authService.getUUID(id));
	   return request;
   }
   
   @GetMapping("/user/file/{uuid}")
   public ResponseEntity<Resource> getImage(@PathVariable("uuid") String uuid) throws IOException {
       Path imagePath = Paths.get(FOLDER_PATH, uuid); // 이미지 파일 경로

       byte[] imageBytes = Files.readAllBytes(imagePath);

       ByteArrayResource resource = new ByteArrayResource(imageBytes);
       System.out.println("resource"+ resource);
       return ResponseEntity.ok()
               .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + uuid)
               .contentType(MediaType.IMAGE_PNG)
               .contentLength(imageBytes.length)
               .body(resource);
   }
//   @GetMapping("/user/file/{uuid}")
//   public ResponseEntity<Resource> getImage(@PathVariable("uuid") String uuid) throws IOException {
//       Path imagePath = Paths.get(FOLDER_PATH, uuid); // 이미지 파일 경로
//
//       byte[] imageBytes = Files.readAllBytes(imagePath);
//
//       ByteArrayResource resource = new ByteArrayResource(imageBytes);
//       System.out.println("resource"+ resource);
//       return ResponseEntity.ok()
//               .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + uuid)
//               .contentType(MediaType.IMAGE_PNG)
//               .contentLength(imageBytes.length)
//               .body(resource);
//   }
// @Operation(summary = "유저프로필 사진")
// @GetMapping("user/file/{uuid}")
// public ResponseEntity<?> userDownImage(@PathVariable("uuid")String uuid)throws IOException {
//	 byte[] downloadImage = authService.downloadImageSystem(uuid);
//	 if(downloadImage != null) {
//		  return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/*"))
//                 .body(downloadImage);
//	 }else {
//		 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//	 }
// }
   
  
   
 //프로필 등록
 	@PostMapping("/uploadprofileimage/{id}")
 	@Operation(summary="프로필 사진 등록")
     public ResponseEntity<UserResponse> uploadProfileImage(@RequestParam("file") MultipartFile file, @PathVariable("id") Long userId) {
         try {
        	 System.out.println("받은파일명 : "+file);
             // AuthService를 사용하여 프로필 이미지 업로드 및 파일 이름 반환
             String uploadedFileName = authService.uploadProfileImage(file,userId);
             System.out.println("업로드 파일명 : " + uploadedFileName);
             // 사용자 엔터티 조회
             UserEntity userEntity = authService.findById(userId);

             // 업로드된 파일 이름을 엔터티의 profileImage 필드에 설정
             userEntity.setProfileImage(uploadedFileName);
             System.out.println("뭐가들어있어 :" + userEntity);
    
             // 엔터티 업데이트
             return ResponseEntity.ok(UserResponse.from(userEntity));
         } catch (IOException e) {
             e.printStackTrace();
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
         }
     }
 	  // 유저피드 페이지
    @PostMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> userFeeds(@PathVariable("id") Long userid,@RequestBody ChatRequestDTO dto) {
        return ResponseEntity.ok(authService.getUserFeed(userid, dto.getMyId()));
    }
    
    // 마이피드 페이지
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> myFeeds(@PathVariable("id") Long userid) {
        return ResponseEntity.ok(authService.getMyFeed(userid));
    }	
  
}
