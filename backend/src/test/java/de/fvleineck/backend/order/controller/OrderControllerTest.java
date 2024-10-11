package de.fvleineck.backend.order.controller;

import de.fvleineck.backend.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	OrderRepository orderRepository;

	@DirtiesContext
	@Test
	void createOrder () throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
							"id": "1",
							"lastname": "Mustermann",
							"firstname": "Max",
							"email": "fabian@doez.info",
							"quantitySmoked": 1,
							"quantityFresh": 2
							}
"""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json("""
						{
							"id": "1",
							"lastname": "Mustermann",
							"firstname": "Max",
							"email": "fabian@doez.info",
							"quantitySmoked": 1,
							"quantityFresh": 2
							}
						"""));
	}

	@DirtiesContext
	@Test
	void getAllOrders () throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/orders"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json("[]"));
	}

	@DirtiesContext
	@Test
	void deleteOrder () throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/api/orders/1"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	}