package xyz.heetaeb.Woute.domain.user.entity;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;

@Entity
@Getter
@Builder
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "w_user")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	@SequenceGenerator(name = "user_seq", sequenceName = "user_SEQ", allocationSize = 1)
	private long id;
	private String nickname;
	private String password;
	private String email;
	private String profileImage;
	private String introduction;
	private String provider;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
	
	public void setPassword(String password) {
		this.password = password;
	}

}
