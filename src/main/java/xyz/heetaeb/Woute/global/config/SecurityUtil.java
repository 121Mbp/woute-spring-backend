package xyz.heetaeb.Woute.global.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
	private SecurityUtil() {}
		public static Long getCurrentMemberId() {
			final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			
			if(authentication == null || authentication.getName() == null) {
				throw new RuntimeException("conetext에 인증정보가 없습니다.");
			}
			return Long.parseLong(authentication.getName());
	}
}
