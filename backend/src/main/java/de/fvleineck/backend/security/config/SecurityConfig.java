package de.fvleineck.backend.security.config;


import de.fvleineck.backend.security.AppuserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	String apiOrders = "/api/orders/**";

	@Bean
	public PasswordEncoder passwordEncoder() {
		return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(sessionManagement ->
						sessionManagement
								.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
				)
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								.requestMatchers(HttpMethod.POST, "api/users/register").permitAll()
								.requestMatchers(HttpMethod.POST, "api/users/login").permitAll()
								// Members
								.requestMatchers(HttpMethod.DELETE, apiOrders).hasAnyRole(AppuserRole.ADMIN.name())
								.requestMatchers(HttpMethod.GET, apiOrders).hasAnyRole(AppuserRole.ADMIN.name(),
										AppuserRole.GROUP1.name())
								.requestMatchers(HttpMethod.PUT, apiOrders).hasAnyRole(AppuserRole.ADMIN.name(), AppuserRole.GROUP1.name())
								.requestMatchers(HttpMethod.POST, apiOrders).permitAll()
								// All other requests
								.anyRequest().permitAll()
				)
				.httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint((request, response, authException) -> response.sendError(401)));
		return http.build();
	}

}