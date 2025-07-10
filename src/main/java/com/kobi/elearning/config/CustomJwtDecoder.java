package com.kobi.elearning.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.kobi.elearning.dto.request.IntrospectRequest;
import com.kobi.elearning.dto.response.IntrospectResponse;
import com.kobi.elearning.service.AuthenticationService;
import com.kobi.elearning.service.JwtServiceImpl;

@Component
public class CustomJwtDecoder implements JwtDecoder {
	@Value("${jwt.secret}")
	private String secretKey;

	@Autowired
	private AuthenticationService authenticationService;

	private NimbusJwtDecoder nimbusJwtDecoder;

	@Override
	public Jwt decode(String token) throws JwtException {
		var response = authenticationService.introspect(IntrospectRequest.builder().token(token).build());
		if (!response.isValid()){
			throw new JwtException("Token is not valid");
		}
		try {
			if (nimbusJwtDecoder == null) {
				SecretKey key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
				nimbusJwtDecoder = NimbusJwtDecoder
						.withSecretKey(key)
						.macAlgorithm(MacAlgorithm.HS512)
						.build();
			}
			return nimbusJwtDecoder.decode(token);
		} catch (Exception e) {
			throw new JwtException("Token decode failed", e);
		}
	}

}
