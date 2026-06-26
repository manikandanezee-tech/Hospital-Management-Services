package com.ezeeinfo.hospitalmanagementservices.util;

import java.util.Date;

import javax.crypto.SecretKey;

import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtill {

	private static final String SECRET = "hospitalmanagementservicessecretkey73";
	static final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

	public static String generateToken(UserDTO userDTO) {

		return Jwts.builder().setSubject(userDTO.getCode()).claim("role", userDTO.getRole().getCode()).claim("userId", userDTO.getId()).claim("namespaceCode", userDTO.getNamespaceDTO().getCode()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 86400000)).signWith(secretKey).compact();
	}

	public static Claims validateToken(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}

}
