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

	String apiMembers = "/api/members/**";
	String cashJournal = "/api/cash-journal/**";
	String toDos = "/api/todos/**";

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
								.requestMatchers(HttpMethod.DELETE, apiMembers).hasAnyRole(AppuserRole.ADMIN.name(),
										AppuserRole.GROUP1.name())
								.requestMatchers(HttpMethod.GET, apiMembers).hasAnyRole(AppuserRole.ADMIN.name(),
										AppuserRole.GROUP1.name())
								.requestMatchers(HttpMethod.PUT, apiMembers).hasAnyRole(AppuserRole.ADMIN.name(), AppuserRole.GROUP1.name())
								.requestMatchers(HttpMethod.POST, apiMembers).hasAnyRole(AppuserRole.ADMIN.name(),
										AppuserRole.GROUP1.name())
								//CashJournal
								.requestMatchers(HttpMethod.DELETE, cashJournal).hasAnyRole(AppuserRole.ADMIN.name(), AppuserRole.GROUP1.name())
								.requestMatchers(HttpMethod.GET, cashJournal).hasAnyRole(AppuserRole.ADMIN.name(), AppuserRole.GROUP1.name())
								.requestMatchers(HttpMethod.PUT, cashJournal).hasAnyRole(AppuserRole.ADMIN.name(), AppuserRole.GROUP1.name())
								.requestMatchers(HttpMethod.POST, cashJournal).hasAnyRole(AppuserRole.ADMIN.name(), AppuserRole.GROUP1.name())
								// To_Dos
								.requestMatchers(HttpMethod.DELETE, toDos).hasAnyRole(AppuserRole.ADMIN.name(), AppuserRole.GROUP1.name())
								.requestMatchers(HttpMethod.GET, toDos).hasAnyRole(AppuserRole.ADMIN.name(), AppuserRole.GROUP1.name())
								.requestMatchers(HttpMethod.PUT, toDos).hasAnyRole(AppuserRole.ADMIN.name(), AppuserRole.GROUP1.name())
								.requestMatchers(HttpMethod.POST, toDos).hasAnyRole(AppuserRole.ADMIN.name(), AppuserRole.GROUP1.name())
								// All other requests
								.anyRequest().permitAll()
				)
				.httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint((request, response, authException) -> response.sendError(401)));
		return http.build();
	}

}