package de.fvleineck.backend.order.service;

import de.fvleineck.backend.order.model.Order;
import de.fvleineck.backend.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderServiceTest {

	private final OrderRepository mockOrderRepository = mock(OrderRepository.class);

	@Test
	void testCreateOrder() {
		Order order = new Order("1", "lastname", "firstname", "email", 1, 1);
		when(mockOrderRepository.save(order)).thenReturn(order);

		OrderService orderService = new OrderService(mockOrderRepository);

		Order actual = orderService.createOrder(order);

		assertEquals(order, actual);
		verify(mockOrderRepository).save(order);
	}

	@Test
	void testGetAllOrders() {
		Order order = new Order("1", "lastname", "firstname", "email", 1, 1);
		when(mockOrderRepository.findAll()).thenReturn(java.util.List.of(order));

		OrderService orderService = new OrderService(mockOrderRepository);

		java.util.List<Order> actual = orderService.getAllOrders();

		assertEquals(java.util.List.of(order), actual);
		verify(mockOrderRepository).findAll();
	}

	@Test
	void testDeleteOrder() {
		OrderService orderService = new OrderService(mockOrderRepository);

		orderService.deleteOrder("1");

		verify(mockOrderRepository).deleteById("1");
	}
}