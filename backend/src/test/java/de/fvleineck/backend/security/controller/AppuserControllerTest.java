package de.fvleineck.backend.security.controller;

import de.fvleineck.backend.security.AppuserRole;
import de.fvleineck.backend.security.models.AppUser;
import de.fvleineck.backend.security.models.AppUserResponse;
import de.fvleineck.backend.security.service.AppuserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AppuserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AppuserService appuserService;

	@BeforeEach
	void setUp() {
		AppUser appUser = new AppUser();
		appUser.setId("1");
		appUser.setUsername("testadmin");
		appUser.setRole(AppuserRole.ADMIN);

		when(appuserService.findByUsername("testadmin")).thenReturn(appUser);
		when(appuserService.getLoggedInUser()).thenReturn(new AppUserResponse("1", "testadmin", AppuserRole.ADMIN));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void getLoggedInUser_returnsUserDetails() throws Exception {
		mockMvc.perform(get("/api/users/me"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("testadmin"))
				.andExpect(jsonPath("$.role").value("ADMIN"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void login_shouldReturnNoContent() throws Exception {
		mockMvc.perform(post("/api/users/login"))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void registerUser_createsUser() throws Exception {
		mockMvc.perform(post("/api/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"newuser\",\"password\":\"password\"}"))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "testadmin", roles = "ADMIN")
	void logout_shouldInvalidateSessionAndReturnNoContent() throws Exception {
		mockMvc.perform(post("/api/users/logout"))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void findAllUsers_returnsAllUsers() throws Exception {
		mockMvc.perform(get("/api/users"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().json("[]"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void updateRole_returnsUpdatedUser() throws Exception {
		AppUserResponse appUserResponse = new AppUserResponse("1", "testadmin", AppuserRole.USER);
		when(appuserService.updateRole("1", appUserResponse)).thenReturn(new AppUser("1", "testadmin",
			"xyz",	AppuserRole.USER));

		mockMvc.perform(put("/api/users/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"testadmin\",\"role\":\"USER\"}"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void deleteUser_shouldReturnNoContent() throws Exception {
		mockMvc.perform(delete("/api/users/1"))
				.andExpect(status().isNoContent());
	}
}