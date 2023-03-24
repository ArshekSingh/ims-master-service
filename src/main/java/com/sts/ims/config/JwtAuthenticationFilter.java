package com.sts.ims.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sts.ims.constant.Constant;
import com.sts.ims.exception.ObjectNotFoundException;
import com.sts.ims.intercepter.TenantContext;
import com.sts.ims.repository.TokenRepository;
import com.sts.ims.response.Response;
import com.sts.ims.service.serviceImpl.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter implements Constant {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final TokenRepository tokenRepository;

	private final ObjectMapper mapper;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String tenantID = request.getHeader("x-tenant-id");
		log.info("RequestURI::" + requestURI + " || Search for X-TenantID  :: " + tenantID);
		if (tenantID == null) {
			response.getOutputStream().write(restResponseBytes(new Response(INVALID_TENANT_IDENTIFIER, HttpStatus.BAD_REQUEST)));
			response.setHeader("Content-Type", "application/json");
			response.setStatus(400);
			return;
		}
		TenantContext.setCurrentTenant(tenantID);
		try {
			final String authHeader = request.getHeader("Authorization");
			final String token;
			final String userEmail;
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}
			token = authHeader.substring(7);
			userEmail = jwtService.extractUsername(token);
			if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
				boolean isTokenValid = tokenRepository.findByToken(token).map(t -> !t.isExpired() && !t.isRevoked())
						.orElse(false);
				if (jwtService.isTokenValid(token, userDetails) && isTokenValid) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);

					Claims claims = jwtService.extractAllClaims(token);
					List<String> authorities = (List<String>) claims.get("authorities");

					List<SimpleGrantedAuthority> authoritiesInToken = new ArrayList<>();

					for (String role : authorities)
						authoritiesInToken.add(new SimpleGrantedAuthority("ROLE_" + role));

					SecurityContextHolder.getContext()
							.setAuthentication(new UsernamePasswordAuthenticationToken(
									SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
									SecurityContextHolder.getContext().getAuthentication().getCredentials(),
									authoritiesInToken));
				} else {
					throw new ObjectNotFoundException("Invalid Token");
				}
			} else {
				throw new ObjectNotFoundException("Invalid Email");
			}
			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException e) {
			log.error("ExpiredJwtException occurred , token expired : {}", e.getMessage(), e);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter()
					.write(mapper.writeValueAsString(new Response("Token Expired", HttpStatus.UNAUTHORIZED)));
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		} catch (ObjectNotFoundException e) {
			log.error("ObjectNotFoundException occurred ", e.getMessage(), e);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter()
					.write(mapper.writeValueAsString(new Response(e.getMessage(), HttpStatus.UNAUTHORIZED)));
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		} catch (SignatureException e) {
			log.error("Tampered token ");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter()
					.write(mapper.writeValueAsString(new Response("Tampered token", HttpStatus.UNAUTHORIZED)));
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		}
	}

	private byte[] restResponseBytes(Response response) throws IOException {
		String serialized = new ObjectMapper().writeValueAsString(response);
		return serialized.getBytes();
	}
}
