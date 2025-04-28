package de.fvleineck.backend.order.controller;

import de.fvleineck.backend.BackendApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest(classes = BackendApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderControllerTest {

	@Autowired
	private MockMvc mvc;


	@Test
	@WithMockUser(roles="ADMIN")
	void createOrder () throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
												{
													"id": "0001-20250619",
													"lastname": "Mustermann",
													"firstname": "Max",
													"email": "fabian@doez.info",
													"phone": "0123456789",
													"quantitySmoked": 1,
													"quantityFresh": 2,
													"pickupPlace": "home",
													"comment": "no comment",
													"pickedUp": false
													}
						"""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json("""
											{
												"id": "0001-20250619",
												"lastname": "Mustermann",
												"firstname": "Max",
												"email": "fabian@doez.info",
												"phone": "0123456789",
												"quantitySmoked": 1,
												"quantityFresh": 2,
												"pickupPlace": "home",
												"comment": "no comment",
												"pickedUp": false
												}
						"""));
	}

	@Test
	@WithMockUser(roles="ADMIN")
	void getAllOrders () throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/orders"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json("[]"));
	}

	@Test
	@WithMockUser(roles="ADMIN")
	void deleteOrder() throws Exception {
		// Create a new order before deleting
		mvc.perform(MockMvcRequestBuilders.post("/api/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
                    {
                        "id": "1",
                        "lastname": "Mustermann",
                        "firstname": "Max",
                        "email": "fabian@doez.info",
                        "phone": "0123456789",
                        "quantitySmoked": 1,
                        "quantityFresh": 2,
						"pickupPlace": "home",
						"comment": "no comment",
						"pickedUp": false
                     }
            """))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Now delete the order
		mvc.perform(MockMvcRequestBuilders.delete("/api/orders/1"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	}