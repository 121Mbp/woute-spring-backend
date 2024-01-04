package xyz.heetaeb.Woute.global.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
	
	private String issuer;
	private String secretkey;
}
