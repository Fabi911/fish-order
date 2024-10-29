package de.fvleineck.backend.security.service;

import de.fvleineck.backend.security.AppuserRole;
import de.fvleineck.backend.security.models.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import static org.mockito.Mockito.*;

@SpringBootTest
class AppUserDetailServiceTest {

	@Autowired
	private AppUserDetailService appUserDetailService;

	@MockBean
	private de.fvleineck.backend.security.service.AppuserService appUserService;

	@Nested
	class LoadUserByUsername {

		@Test
		void loadUserByUsername_WhenUsernameExists_ReturnsUser() {

			AppUser appUser = new AppUser("1","username", "password", AppuserRole.USER);
			when(appUserService.findByUsername("username")).thenReturn(appUser);


			UserDetails result = appUserDetailService.loadUserByUsername("username");


			Assertions.assertEquals(appUser.getUsername(), result.getUsername());
			Assertions.assertEquals(appUser.getPassword(), result.getPassword());
			verify(appUserService, times(1)).findByUsername("username");
		}

		@Test
		void loadUserByUsername_WhenUsernameDoesNotExist_ThrowsException() {

			when(appUserService.findByUsername("username")).thenThrow(new UsernameNotFoundException("User not found"));


			Assertions.assertThrows(UsernameNotFoundException.class, () -> {
				appUserDetailService.loadUserByUsername("username");
			});
			verify(appUserService, times(1)).findByUsername("username");
		}
	}
}