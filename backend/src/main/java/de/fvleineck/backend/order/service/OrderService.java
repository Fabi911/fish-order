package de.fvleineck.backend.order.service;

import de.fvleineck.backend.order.model.Order;

import de.fvleineck.backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final EmailService emailService;
	private static final Logger logger = Logger.getLogger(OrderService.class.getName());

	public Order createOrder(Order newOrder) {
		Order savedOrder = orderRepository.save(newOrder);
		sendOrderConfirmationEmail(savedOrder);
		return savedOrder;
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public void deleteOrder(String id) {
		orderRepository.deleteById(id);
	}

	private void sendOrderConfirmationEmail(Order order) {
		String subject = "Bestellbestätigung";
		StringBuilder textBuilder = new StringBuilder();
		textBuilder.append("Danke für ihre Bestellung, ")
				.append(order.firstname())
				.append(" ")
				.append(order.lastname())
				.append("!<br><br>") // Use <br> for line breaks
				.append("Bestellungsdetails:<br>")
				.append("- geräucherte Forelle: ")
				.append(order.quantitySmoked())
				.append("<br>")
				.append("- eingelegte Forelle: ")
				.append(order.quantityFresh())
				.append("<br><br>")
				.append("freundlichste Grüße,<br><br> Fischerei- und Hegeverein Leineck e.V.");
		String text = textBuilder.toString();
		try {
			emailService.sendOrderConfirmation(order.email(), subject, text);
			logger.info("Order confirmation email sent to: " + order.email());
		} catch (Exception e) {
			logger.severe("Failed to send order confirmation email: " + e.getMessage());
		}
	}
}