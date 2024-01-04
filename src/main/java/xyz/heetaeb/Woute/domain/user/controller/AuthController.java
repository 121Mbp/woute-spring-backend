package xyz.heetaeb.Woute.domain.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.user.dto.TokenDto;
import xyz.heetaeb.Woute.domain.user.dto.request.UpdateProfileRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UserLog;
import xyz.heetaeb.Woute.domain.user.dto.request.UserRequest;
import xyz.heetaeb.Woute.domain.user.dto.response.UserResponse;
import xyz.heetaeb.Woute.domain.user.service.AuthService;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
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
		System.out.println("aa : "+requestDto.toString());
        return ResponseEntity.ok(authService.login(requestDto));
    }
   @Operation(summary = "프로필 수정")
   @PutMapping("/modifyprofile/{id}")
   public ResponseEntity<UserResponse> updateProfile(
           @PathVariable Long id,
           @RequestBody UpdateProfileRequest updateRequest) {
       return ResponseEntity.ok(authService.updateUserProfile(id, updateRequest));
   }
//   @Operation(summary = "프로필수정")
//   @GetMapping("/modifyprofile/{id}")
//   public List<UserResponse>getUserList(@PathVariable(user_id)Long user_id){
//	   return authService.userList(user_id);
//   }
}
