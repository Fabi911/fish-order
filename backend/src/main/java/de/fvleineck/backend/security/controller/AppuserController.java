package de.fvleineck.backend.security.controller;


import de.webdev.backend.security.models.AppUser;
import de.webdev.backend.security.models.AppUserDTO;
import de.webdev.backend.security.models.AppUserResponse;
import de.webdev.backend.security.service.AppuserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AppuserController {
	private final AppuserService appuserService;

	@GetMapping("/me")
	public AppUserResponse getLoggedInUser() {
		return appuserService.getLoggedInUser();
	}

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void login() {
		// Method is empty because the user is already logged in
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public AppUserResponse register(@RequestBody AppUserDTO appUserDTO) {
		return appuserService.registerUser(appUserDTO);
	}

	@PostMapping("/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void logout(HttpSession session) {
		session.invalidate();
		SecurityContextHolder.clearContext();
	}

	@GetMapping
	public List<AppUserResponse> findAllUsers() {
		return appuserService.findAllUsers();
	}

	@PutMapping("{id}")
	public AppUser updateRole(@PathVariable String id, @RequestBody AppUserResponse appUserResponse) {
		return appuserService.updateRole(id, appUserResponse);
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable String id) {
		appuserService.deleteUser(id);
	}
}