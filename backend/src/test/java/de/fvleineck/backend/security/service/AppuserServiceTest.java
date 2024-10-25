package de.fvleineck.backend.security.service;

import de.fvleineck.backend.security.AppuserRole;
import de.fvleineck.backend.security.models.AppUser;
import de.fvleineck.backend.security.models.AppUserDTO;
import de.fvleineck.backend.security.models.AppUserResponse;
import de.fvleineck.backend.security.repository.AppuserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppuserServiceTest {

	@Mock
	private AppuserRepository appuserRepository;

	@InjectMocks
	private AppuserService appuserService;

	@Test
	void findByUsernameReturnsUserWhenUserExists() {
		String testUsername = "testUser";
		AppUser appUser = new AppUser();
		appUser.setUsername(testUsername);
		when(appuserRepository.findByUsername(testUsername)).thenReturn(Optional.of(appUser));

		AppUser returnedUser = appuserService.findByUsername(testUsername);

		assertEquals(appUser.getUsername(), returnedUser.getUsername(), "Returned user has the expected username");
	}

	@Test
	void findByUsernameThrowsWhenUserDoesNotExist() {
		String testUsername = "testUser";
		when(appuserRepository.findByUsername(testUsername)).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> {
			appuserService.findByUsername(testUsername);
		}, "Expected findByUsername to throw, but it didn't");
	}

	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	void registerUserCreatesNewUserWhenUsernameIsUnique() {
		String testUsername = "testUser";
		String testPassword = "password";
		String encodedPassword = "encodedPassword"; // Mock encoding result

		AppUserDTO appUserDTO = new AppUserDTO(testUsername, testPassword);
		AppUser appUser = new AppUser();
		appUser.setId(UUID.randomUUID().toString());
		appUser.setUsername(testUsername);
		appUser.setPassword(encodedPassword); // Set encoded password
		appUser.setRole(AppuserRole.USER);
		AppUserResponse appUserResponse = new AppUserResponse(appUser.getId(), appUser.getUsername(), appUser.getRole());

		when(passwordEncoder.encode(testPassword)).thenReturn(encodedPassword); // Mock encoding
		when(appuserRepository.findByUsername(testUsername)).thenReturn(Optional.empty());
		when(appuserRepository.save(ArgumentMatchers.any(AppUser.class))).thenReturn(appUser);

		AppUserResponse result = appuserService.registerUser(appUserDTO);

		assertEquals(appUserResponse.id(), result.id());
		assertEquals(appUserResponse.username(), result.username());
		assertEquals(appUserResponse.role(), result.role());
		assertEquals(encodedPassword, appUser.getPassword()); // Verify password encoding
	}

	@Test
	void registerUserThrowsWhenUsernameIsNotUnique() {
		String testUsername = "testUser";
		String testPassword = "password";
		AppUserDTO appUserDTO = new AppUserDTO(testUsername, testPassword);
		AppUser existingUser = new AppUser();
		existingUser.setUsername(testUsername);
		existingUser.setPassword(testPassword);
		existingUser.setRole(AppuserRole.USER);

		when(appuserRepository.findByUsername(testUsername)).thenReturn(Optional.of(existingUser));

		assertThrows(DataIntegrityViolationException.class, () -> {
			appuserService.registerUser(appUserDTO);
		});
	}

	@Test
	void findAllUsersReturnsListOfUsers() {
		AppUser user1 = new AppUser();
		user1.setId(UUID.randomUUID().toString());
		user1.setUsername("user1");
		user1.setPassword("password1");
		user1.setRole(AppuserRole.USER);
		AppUser user2 = new AppUser();
		user2.setId(UUID.randomUUID().toString());
		user2.setUsername("user2");
		user2.setPassword("password2");
		user2.setRole(AppuserRole.ADMIN);

		when(appuserRepository.findAll()).thenReturn(List.of(user1, user2));

		var result = appuserService.findAllUsers();

		assertEquals(2, result.size(), "Expected 2 users in the result");
		assertEquals(user1.getId(), result.get(0).id());
		assertEquals(user1.getUsername(), result.get(0).username());
		assertEquals(user1.getRole(), result.get(0).role());
		assertEquals(user2.getId(), result.get(1).id());
		assertEquals(user2.getUsername(), result.get(1).username());
		assertEquals(user2.getRole(), result.get(1).role());
	}

	@Test
	void updateRoleReturnsUpdatedUser() {
		String testId = UUID.randomUUID().toString();
		AppUser appUser = new AppUser();
		appUser.setId(testId);
		appUser.setUsername("testUser");
		appUser.setPassword("password");
		appUser.setRole(AppuserRole.USER);
		AppUserResponse appUserResponse = new AppUserResponse(appUser.getId(), appUser.getUsername(), appUser.getRole());

		when(appuserRepository.findById(testId)).thenReturn(Optional.of(appUser));
		when(appuserRepository.save(appUser)).thenReturn(appUser);

		AppUser result = appuserService.updateRole(testId, appUserResponse);

		assertEquals(appUser.getId(), result.getId());
		assertEquals(appUser.getUsername(), result.getUsername());
		assertEquals(appUser.getRole(), result.getRole());
	}

	@Test
	void updateRoleThrowsWhenUserDoesNotExist() {
		String testId = UUID.randomUUID().toString();
		AppUserResponse appUserResponse = new AppUserResponse(testId, "testUser", AppuserRole.USER);

		when(appuserRepository.findById(testId)).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> {
			appuserService.updateRole(testId, appUserResponse);
		});
	}

	@Test
	void deleteUserCallsDeleteOnRepository() {
		String testId = UUID.randomUUID().toString();

		appuserService.deleteUser(testId);

		verify(appuserRepository).deleteById(testId);
	}
}