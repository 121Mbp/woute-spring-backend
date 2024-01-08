package xyz.heetaeb.Woute.domain.user.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails extends User {

    private final long userId;
    private final String email;

    public CustomUserDetails(UserEntity userEntity) {
        super(userEntity.getEmail(), userEntity.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        this.userId = userEntity.getId();
        this.email = userEntity.getEmail();
    }

    public long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

	@Override
	public String getUsername() {
		return email;
	}
    
}
