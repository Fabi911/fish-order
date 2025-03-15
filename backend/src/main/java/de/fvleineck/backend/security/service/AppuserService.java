package de.fvleineck.backend.security.service;


import de.fvleineck.backend.security.AppuserRole;
import de.fvleineck.backend.security.models.AppUser;
import de.fvleineck.backend.security.models.AppUserDTO;
import de.fvleineck.backend.security.models.AppUserResponse;
import de.fvleineck.backend.security.repository.AppuserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppuserService {

	private final AppuserRepository appuserRepository;
	private final PasswordEncoder passwordEncoder;

	public AppUser findByUsername(String username) {
		return appuserRepository
				.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	public AppUserResponse getLoggedInUser() {
		var principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AppUser appUser = findByUsername(principal.getUsername());
		return new AppUserResponse(appUser.getId(), appUser.getUsername(), appUser.getRole());
	}

	public AppUserResponse registerUser(AppUserDTO appUserDTO) {
		if (appuserRepository.findByUsername(appUserDTO.username()).isPresent()) {
			throw new DataIntegrityViolationException("Username already taken");
		}
		AppUser appUser = new AppUser();
		appUser.setUsername(appUserDTO.username());
		appUser.setPassword(passwordEncoder.encode(appUserDTO.password()));
		appUser.setRole(AppuserRole.USER);
		appUser = appuserRepository.save(appUser);
		return new AppUserResponse(appUser.getId(), appUser.getUsername(), appUser.getRole());
	}

	public List<AppUserResponse> findAllUsers() {
		return appuserRepository.findAll().stream()
				.map(appUser -> new AppUserResponse(appUser.getId(), appUser.getUsername(), appUser.getRole()))
				.toList();
	}

	public AppUser updateRole(String id, AppUserResponse appUserResponse) {
		AppUser appUser =
				appuserRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(
						"User not found"));
		appUser.setRole(AppuserRole.valueOf(appUserResponse.role().name()));
		return appuserRepository.save(appUser);
	}

	public void deleteUser(String id) {
		appuserRepository.deleteById(id);
	}
}