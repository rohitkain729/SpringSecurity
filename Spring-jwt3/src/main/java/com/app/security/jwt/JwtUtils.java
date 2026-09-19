package com.app.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.app.security.services.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {

	@Value("${jwtSecret}")
	private String jwtSecret;
	@Value("${jwtExp}")
	private int jwtExpirationMs;

//	private Key key() {
//		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//	}

	private SecretKey key() {
		byte[] bytes = Base64.getDecoder().decode(jwtSecret.getBytes(StandardCharsets.UTF_8));
		return new SecretKeySpec(bytes, "HmacSHA256");
	}

	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder().subject(userPrincipal.getUsername()).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtExpirationMs)).signWith(key()).compact();
	}

	public String getUsernameFromToken(String token) {
		Claims claims=Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
		
//		return  Jwts.parser().setSigningKey(key())
//				.build()
//				.parseClaimsJws(token).getBody().getSubject();

		return claims.getSubject();

	}

	private  Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().verifyWith(key()).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			System.out.println(e.getMessage());
		} catch (ExpiredJwtException e) {
			System.out.println(e.getMessage());
		} catch (UnsupportedJwtException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

}
