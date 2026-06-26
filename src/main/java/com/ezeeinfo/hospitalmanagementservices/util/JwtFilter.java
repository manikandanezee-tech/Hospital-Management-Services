package com.ezeeinfo.hospitalmanagementservices.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import com.ezeeinfo.hospitalmanagementservices.dto.AuthDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.NamespaceDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.UserDTO;
import com.ezeeinfo.hospitalmanagementservices.dto.enumeration.UserRoleEM;

import io.jsonwebtoken.Claims;

@Component
public class JwtFilter implements Filter {
	

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String path = httpServletRequest.getRequestURI();

		if (path.contains("/auth/login")) {
			chain.doFilter(request, response);
			return;
		}
		String authHeader = httpServletRequest.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			httpResponse.setContentType("application/json");

			httpResponse.getWriter().write("{\"message\":\"Token Missing\"}");

			return;
		}
		String token = authHeader.substring(7);
		try {
			Claims claims = JwtUtill.validateToken(token);
			
			NamespaceDTO namespaceDTO = new NamespaceDTO();
			namespaceDTO.setCode(claims.get("namespaceCode").toString());
			
			UserDTO userDTO =new UserDTO();
			userDTO.setId(Integer.parseInt(claims.get("userId").toString()));
			userDTO.setRole(UserRoleEM.getUserRoleEMByCode(claims.get("role").toString()));
			userDTO.setNamespaceDTO(namespaceDTO);
			
			AuthDTO authDTO = new AuthDTO();
			authDTO.setUserDTO(userDTO);
			
			httpServletRequest.setAttribute("authDTO", authDTO);
			
//			httpServletRequest.setAttribute("userId", claims.get("userId"));
//			httpServletRequest.setAttribute("role", claims.get("role"));
//			httpServletRequest.setAttribute("namespaceCode", claims.get("namespaceCode"));

		}
		catch (Exception e) {

			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			httpResponse.setContentType("application/json");

			httpResponse.getWriter().write("{\"message\":\"Invalid Token\"}");

			return;
		}
		chain.doFilter(request, response);

	}
	
}
