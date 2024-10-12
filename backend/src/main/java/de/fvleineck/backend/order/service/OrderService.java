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
	private int orderCounter = 0;

	private String generateOrderId() {
		orderCounter++;
		String year="2024";
		String month="12";
		String day="22";
		return String.format("%04d", orderCounter) + "-" + year + month + day;
	}

	public Order createOrder(Order newOrder) {
		String orderId;
		do {
			orderId = generateOrderId();
		} while (orderRepository.existsById(orderId));
		newOrder = new Order(orderId, newOrder.lastname(), newOrder.firstname(), newOrder.email(),newOrder.pickupPlace(), newOrder.comment(),
				newOrder.quantitySmoked(), newOrder.quantityFresh());
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

	public void sendOrderConfirmationEmail(Order order) {
		String subject = "Bestellbestätigung "+ order.id();
		StringBuilder textBuilder = new StringBuilder();
		textBuilder.append("Danke für ihre Bestellung, ")
				.append(order.firstname())
				.append(" ")
				.append(order.lastname())
				.append("!<br><br>") // Use <br> for line breaks
				.append("Bestelldetails:<br>")
				.append("Bestellnummer: ")
				.append(order.id())
				.append("<br><br>")
				.append("- geräucherte Forelle: ")
				.append(order.quantitySmoked())
				.append("<br>")
				.append("- eingelegte Forelle: ")
				.append(order.quantityFresh())
				.append("<br><br>")
				.append("Abholort: ")
				.append(order.pickupPlace())
				.append("<br><br>")
				.append("Kommentar: ")
				.append(order.comment())
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

	public int getTotalQuantitySmoked() {
		return orderRepository.findAll().stream().mapToInt(Order::quantitySmoked).sum();
	}

	public int getTotalQuantityFresh() {
		return orderRepository.findAll().stream().mapToInt(Order::quantityFresh).sum();
	}
}