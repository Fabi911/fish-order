package de.fvleineck.backend.order.service;

import de.fvleineck.backend.order.model.Order;
import de.fvleineck.backend.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderServiceTest {

	private final OrderRepository mockOrderRepository = mock(OrderRepository.class);
	private final EmailService mockEmailService = mock(EmailService.class);

	@Test
	void testCreateOrder() {
		Order order = new Order(null, "lastname", "firstname", "email","0123456789", "home", "", 1, 1,false);
		Order savedOrder = new Order("0001-20250418", "lastname", "firstname", "email", "0123456789","home", "", 1, 1
				,false);
		when(mockOrderRepository.save(any(Order.class))).thenReturn(savedOrder);

		OrderService orderService = new OrderService(mockOrderRepository, mockEmailService);

		Order actual = orderService.createOrder(order);

		assertEquals(savedOrder, actual);
		verify(mockOrderRepository).save(any(Order.class));
	}

	@Test
	void testGetAllOrders() {
		Order order = new Order("1", "lastname", "firstname", "email", "0123456789","home", "", 1, 1,false);
		when(mockOrderRepository.findAll()).thenReturn(java.util.List.of(order));

		OrderService orderService = new OrderService(mockOrderRepository,mockEmailService);

		java.util.List<Order> actual = orderService.getAllOrders();

		assertEquals(java.util.List.of(order), actual);
		verify(mockOrderRepository).findAll();
	}

	@Test
	void testDeleteOrder() {
		OrderService orderService = new OrderService(mockOrderRepository,mockEmailService);

		orderService.deleteOrder("1");

		verify(mockOrderRepository).deleteById("1");
	}
}