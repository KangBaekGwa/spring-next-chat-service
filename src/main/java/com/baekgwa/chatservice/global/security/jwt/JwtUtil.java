package com.baekgwa.chatservice.global.security.jwt;

import static com.baekgwa.chatservice.global.security.constant.JwtConstant.*;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.baekgwa.chatservice.global.environment.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.chatservice.global.security.jwt
 * FileName    : JwtProperties
 * Author      : Baekgwa
 * Date        : 2025-10-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-10-02     Baekgwa               Initial creation
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

	private final JwtProperties jwtProperties;
	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
	}

	public boolean isExpired(String token) {
		Claims claims = parseClaims(token);
		if (claims == null) {
			return true;
		}
		return claims.getExpiration().before(new Date());
	}

	public String createJwt(Long userId) {
		return Jwts.builder()
			.issuedAt(Date.from(Instant.now()))
			.expiration(Date.from(Instant.now().plusSeconds(jwtProperties.getTokenExpirationMin() * 60)))
			.claim(CLAIM_KEY_USER_ID, userId)
			.signWith(secretKey)
			.compact();
	}

	public Long getUserId(String token) {
		Claims claims = parseClaims(token);
		if (claims == null) {
			return null;
		}
		return claims.get(CLAIM_KEY_USER_ID, Long.class);
	}

	public Date getExpirationDate(String token) {
		Claims claims = parseClaims(token);
		if (claims == null) {
			return null;
		}
		return claims.getExpiration();
	}

	/**
	 * 토큰을 파싱하여 Claims 정보를 반환하는 private 헬퍼 메서드
	 * @param token JWT 토큰
	 * @return Claims 객체, 파싱 실패 시 null 반환
	 */
	private Claims parseClaims(String token) {
		try {
			return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		} catch (Exception e) {
			return null; // 파싱 실패 시 null 반환
		}
	}
}

