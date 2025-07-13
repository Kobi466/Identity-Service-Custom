package com.kobi.elearning.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	private static final String[] PUBLIC_URLS = {
			"/api/v1/auth/login",
			"/api/v1/users/register",
			"/api/v1/auth/introspect",
			"/api/v1/auth/logout",
			"/api/v1/auth/refresh",
	};

	CustomJwtDecoder jwtDecoder;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(requests ->
				requests
					.requestMatchers(HttpMethod.POST, PUBLIC_URLS)
						.permitAll()
						.anyRequest()
						.authenticated()
		);
		http.oauth2ResourceServer(resourceServer -> resourceServer
				.jwt(jwt -> jwt
						.decoder(jwtDecoder)
						.jwtAuthenticationConverter(jwtAuthenticationConverter()))
		);
		http.csrf(AbstractHttpConfigurer::disable);
		return http.build();
	}

	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
}
