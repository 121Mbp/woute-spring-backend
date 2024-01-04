package xyz.heetaeb.Woute.global.config.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import xyz.heetaeb.Woute.domain.user.dto.TokenDto;


@Slf4j
@Component
//Token의 생성, 인증정보 조회, 유효성 검증, 암호화 설정 등의 역할을 하는 클래스
public class TokenProvider {
	private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private final Key key;
    
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        System.out.println("key : "+key);
    }


    
    //토큰생성
    public TokenDto generateTokenDto(Authentication authentication) {
    	String authorities = authentication.getAuthorities().stream()
    			.map(GrantedAuthority::getAuthority)
    			.collect(Collectors.joining(","));
    	long now = (new Date().getTime());
    	
    	Date tokenExpiresIn = new Date (now + ACCESS_TOKEN_EXPIRE_TIME);
    	System.out.println("tokenExpiresIn : " + tokenExpiresIn );
    	
    	String accessToken = Jwts.builder()
    			.setSubject(authentication.getName())
    			.claim(AUTHORITIES_KEY, authorities)
    			.setExpiration(tokenExpiresIn)
    			.signWith(SignatureAlgorithm.HS256, key)
    			.compact();
    	System.out.println(accessToken);
    	return TokenDto.builder()
    			.grantType(BEARER_TYPE)
    			.accessToken(accessToken)
    			.tokenExpiresIn(tokenExpiresIn.getTime())
    			.build();
    }
    
    public Authentication getAuthentication(String accessToken) {
    	Claims claims = parseClaims(accessToken);
    	System.out.println("accessToken : " + accessToken);
    	if(claims.get(AUTHORITIES_KEY) == null) {
    		throw new RuntimeException("권한 정보가 없는 토큰");
    	}
    	
    	Collection<? extends GrantedAuthority> authorities = 
    			Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
    			.map(SimpleGrantedAuthority::new)
    			.collect(Collectors.toList());
    	
    	UserDetails principal = new User(claims.getSubject(), "", authorities);
    	
    	return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
  
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

	
}
