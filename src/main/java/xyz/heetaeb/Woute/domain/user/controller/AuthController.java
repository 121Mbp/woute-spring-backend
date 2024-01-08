package xyz.heetaeb.Woute.domain.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.user.dto.TokenDto;
import xyz.heetaeb.Woute.domain.user.dto.request.UpdateProfileRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UserLog;
import xyz.heetaeb.Woute.domain.user.dto.request.UserRequest;
import xyz.heetaeb.Woute.domain.user.dto.response.UserResponse;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;
import xyz.heetaeb.Woute.domain.user.service.AuthService;
//import xyz.heetaeb.Woute.domain.user.service.EmailService;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {
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
   @Operation(summary = "프로필 수정")
   @PutMapping("/modifyprofile/{id}")
   public ResponseEntity<UserResponse> updateProfile(
           @PathVariable Long id,
           @RequestBody UpdateProfileRequest updateRequest) {
       return ResponseEntity.ok(authService.updateUserProfile(id, updateRequest));
   }
 
   @Operation(summary = "토큰 유저정보")
   @PostMapping("/userinfosave")
   public ResponseEntity<UserEntity> userInfoSave(@RequestParam Long id) {
       System.out.println("토큰에 있는 id: " + id);
       System.out.println(authService.findById(id));
       return ResponseEntity.ok(authService.findById(id));
   }
   
//   @Operation(summary = "프로필수정")
//   @GetMapping("/modifyprofile/{id}")
//   public List<UserResponse>getUserList(@PathVariable(user_id)Long user_id){
//	   return authService.userList(user_id);
//   }
   
//   @Operation(summary = "인증메일 전송")
//   @PostMapping("/mailSender")
//   public String mailSender(@RequestBody String userEmail) throws Exception {
//	   System.out.println("request : " + userEmail);
//	   String resultError = "N";
//	   String resultOk = "Y";
//	   	System.out.println("request : " + userEmail);
//		boolean exist = authService.existsByEmail(userEmail);
//		System.out.println("존재하냐 2 : " + exist);
//		List<UserEntity> selectList = authService.selectAll();
//		for(UserEntity u : selectList) {
//			System.out.println(u.toString());
//		}
		
//		if(!exist) {
//			System.out.println("새이용자");
//			return resultOk;
//		}else {
//			System.out.println("기존이용자");
//			return resultError;
//		}
//		
		
		
		//String confirm = emailService.sendSimpleMessage(userEmail);
		//type A = 인증이메일 전송 완료
		
		//type B = 인증이메일 전송 실패
      
//   }
//   @Operation(summary = "인증코드 일치여부")
//   @PostMapping("/join/verifycode")
//   public String verifyCode(@RequestBody String verifyCode) throws Exception{
//	   System.out.println("화면코드 : " + verifyCode);
//	   String resultOk ="Y";
//	   String resultError ="N";
//	   String check = "aaa";
//	 
//	   if(check.equals(verifyCode)) {
//		   System.out.println(resultOk);
//		   return "Y";
//	   }else {
//		   System.out.println(resultError);
//		   return "N";
//		   
//	   }
//
//   }
  
}
