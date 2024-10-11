package de.fvleineck.backend.order.controller;


import de.fvleineck.backend.order.model.Order;
import de.fvleineck.backend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	// @PostMapping

	@PostMapping()
	public Order createOrder(@RequestBody Order newOrder) {
		return orderService.createOrder(newOrder);
	}

	@GetMapping()
	public List<Order> getAllOrders() {
		return orderService.getAllOrders();
	}

	@DeleteMapping("/{id}")
	public void deleteOrder(@PathVariable String id) {
		orderService.deleteOrder(id);
	}
}