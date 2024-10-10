package de.fvleineck.backend.order.service;

import de.fvleineck.backend.order.model.Order;
import de.fvleineck.backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	public Order createOrder(Order order) {
		return orderRepository.save(order);
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public void deleteOrder(String id) {
		orderRepository.deleteById(id);
	}
}