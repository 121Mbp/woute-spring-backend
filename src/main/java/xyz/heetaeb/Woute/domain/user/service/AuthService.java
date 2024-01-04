package xyz.heetaeb.Woute.domain.user.service;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.user.dto.TokenDto;
import xyz.heetaeb.Woute.domain.user.dto.request.UpdateProfileRequest;
import xyz.heetaeb.Woute.domain.user.dto.request.UserLog;
import xyz.heetaeb.Woute.domain.user.dto.request.UserRequest;
import xyz.heetaeb.Woute.domain.user.dto.response.UserResponse;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;
import xyz.heetaeb.Woute.global.config.jwt.TokenProvider;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

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

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        System.out.println(authenticationToken);
        System.out.println("authentiation 제발 여기 통과좀 해줘");
        return tokenProvider.generateTokenDto(authentication);
    }
    
    public UserEntity getCurrentUser() {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Optional에서 UserEntity로 변환
        return userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. Email: " + currentUsername));
    }

    public UserResponse updateUserProfile(Long userId, UpdateProfileRequest updateRequest) {
        UserEntity currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("수정할 수 없는 사용자입니다."));

        if (updateRequest.getNickname() != null) {
            currentUser.setNickname(updateRequest.getNickname());
            System.out.println("닉네임 수정 : "+ updateRequest.getNickname());
        }
        if (updateRequest.getIntroduction() != null) {
            currentUser.setIntroduction(updateRequest.getIntroduction());
            System.out.println("자기소개 수정");
        }
        if (updateRequest.getPassword() != null) {
            currentUser.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
            System.out.println("비밀번호 수정 : " + updateRequest.getPassword());
        }
        if (updateRequest.getProfileImage() != null) {
            currentUser.setProfileImage(updateRequest.getProfileImage());
            System.out.println("프로필 사진 수정");
        }

        currentUser.setUpdatedAt(ZonedDateTime.now());

     
        UserEntity updatedUser = userRepository.save(currentUser);

        return UserResponse.from(updatedUser);
    }

//    public List<UserResponse> userList(Long id) {
//    	
//    }
}
