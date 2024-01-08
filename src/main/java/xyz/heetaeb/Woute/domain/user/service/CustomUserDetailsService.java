package xyz.heetaeb.Woute.domain.user.service;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByEmailIgnoreCase(username)
				.map(this::createUserDetails)
				.orElseThrow(()-> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));
	}
	 private UserDetails createUserDetails(UserEntity user) {

	        return new User(
	                String.valueOf(user.getId()),
	                user.getPassword(),
	                Collections.emptySet()
	        );
	    }
	
}
