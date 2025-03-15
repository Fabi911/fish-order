package de.fvleineck.backend.order.controller;


import de.fvleineck.backend.order.model.Order;
import de.fvleineck.backend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	// @PostMapping

	@PostMapping()
	public ResponseEntity<Order> createOrder(@RequestBody Order order) {
		Order savedOrder = orderService.createOrder(order);
		return ResponseEntity.ok(savedOrder);
	}

	@GetMapping()
	public List<Order> getAllOrders() {
		return orderService.getAllOrders();
	}

	@DeleteMapping("/{id}")
	public void deleteOrder(@PathVariable String id) {
		orderService.deleteOrder(id);
	}

	@GetMapping("/smoked")
	public int getSmokedFishCount() {
		return orderService.getTotalQuantitySmoked();
	}

	@GetMapping("/fresh")
	public int getFreshFishCount() {
		return orderService.getTotalQuantityFresh();
	}

	@PutMapping("/{id}")
	public Order updateOrder(@PathVariable String id, @RequestBody Order order) {
		return orderService.updateOrder(id, order);
	}
}