//package xyz.heetaeb.Woute.domain.user.controller;
//
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import io.swagger.v3.oas.annotations.Operation;
//
//import lombok.RequiredArgsConstructor;
//import xyz.heetaeb.Woute.domain.user.dto.request.JoinRequest;
//import xyz.heetaeb.Woute.domain.user.dto.request.UserRequest;
//import xyz.heetaeb.Woute.domain.user.dto.response.UserResponse;
//import xyz.heetaeb.Woute.domain.user.service.UserService;
//
//@RestController
//@RequiredArgsConstructor
//public class UserController {
//	private final UserService userService;
//	
//	@Operation(summary = "로그인")
//	@PostMapping("/login")
//	public String login (@RequestBody UserRequest request) {
//		return userService.login(request);
//	}
//	
//	@Operation(summary = "회원가입")
//	@PostMapping("/join")
//	public String joinUser(@RequestBody JoinRequest request) {
//		try {
//			System.out.println("email :"+request.getEmail());
//			System.out.println("nickname :" +request.getNickname());
//			System.out.println("password :"+request.getPassword());
//			Long userId = userService.join(request);
//			return "회원가입성공" + userId;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "회원가입실패";
//		}
//	}
//}
