package xyz.heetaeb.Woute.domain.user.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.service.AuthService;
import xyz.heetaeb.Woute.domain.user.service.EmailService;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class MailController {
		private final AuthService authService;
		private final EmailService emailService;
//		
//	   @Operation(summary = "인증메일 전송")
//	   @PostMapping("/mailSender")
//	   public String mailSender(@RequestParam String userEmail) throws Exception {
//			
//			String resultType = "";
//			String confirm = emailService.sendSimpleMessage(userEmail);
//			System.out.println("confirm : " + userEmail);
//			//type A = 인증이메일 전송 완료
//			resultType = "A";
//			//type B = 인증이메일 전송 실패
//	       return resultType;
//	   }
	 @Operation(summary = "인증코드보내기")
	 @PostMapping("/join/emailConfirm")
	   public String emailConfirm(@RequestBody String email) throws Exception {
		 boolean exist = authService.isEmailAlreadyExists(email);
		 UserEntity id = authService.findById(51L);
		 System.out.println(id);
		 System.out.println(exist);
		 if(!exist) {
	     String confirm = emailService.sendSimpleMessage(email);
	     return confirm;
		 }else {
			 return "";
		 }
	   }
}
