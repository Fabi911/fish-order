package de.fvleineck.backend.order.service;

import de.fvleineck.backend.order.OrderNotFoundException;
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

	// Generate a unique order id
	private String generateOrderId() {
		orderCounter++;
		String year = "2024";
		String month = "12";
		String day = "22";
		return String.format("%04d", orderCounter) + "-" + year + month + day;
	}

	public Order createOrder(Order newOrder) {
		String orderId;
		do {
			orderId = generateOrderId();
		} while (orderRepository.existsById(orderId));
		newOrder = new Order(orderId, newOrder.lastname(), newOrder.firstname(), newOrder.email(),
				newOrder.phone(), newOrder.pickupPlace(), newOrder.comment(),
				newOrder.quantitySmoked(), newOrder.quantityFresh(), newOrder.pickedUp());
		Order savedOrder = orderRepository.save(newOrder);
		if (savedOrder != null) {
			sendOrderConfirmationEmail(savedOrder);
		} else {
			logger.severe("Failed to save order: " + newOrder);
		}
		return savedOrder;
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public void deleteOrder(String id) {
		orderRepository.deleteById(id);
	}

	public Order updateOrder(String id, Order updatedOrder) {
		Order existingOrder = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
		Order editedOrder = new Order(
				id, // Keep the same id
				updatedOrder.lastname() != null ? updatedOrder.lastname() : existingOrder.lastname(),
				updatedOrder.firstname() != null ? updatedOrder.firstname() : existingOrder.firstname(),
				updatedOrder.email() != null ? updatedOrder.email() : existingOrder.email(),
				updatedOrder.phone() != null ? updatedOrder.phone() : existingOrder.phone(),
				updatedOrder.pickupPlace() != null ? updatedOrder.pickupPlace() : existingOrder.pickupPlace(),
				updatedOrder.comment() != null ? updatedOrder.comment() : existingOrder.comment(),
				updatedOrder.quantitySmoked() != null ? updatedOrder.quantitySmoked() : existingOrder.quantitySmoked(),
				updatedOrder.quantityFresh() != null ? updatedOrder.quantityFresh() : existingOrder.quantityFresh(),
				updatedOrder.pickedUp() != null ? updatedOrder.pickedUp() : existingOrder.pickedUp()
		);
		return orderRepository.save(editedOrder);
	}

	// Send an order confirmation email to the customer
	public void sendOrderConfirmationEmail(Order order) {
		String subject = "ForellenBestellbestätigung " + order.id();
		StringBuilder textBuilder = new StringBuilder();
		textBuilder.append("Danke für ihre Bestellung, ")
				.append(order.firstname())
				.append(" ")
				.append(order.lastname())
				.append("!<br><br>") // Use <br> for line breaks
				.append("Bestelldetails:<br>")
				.append("Verkaufsdatum: Sonntag, 22.12.2024<br>")
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
				.append("<br>");
		if ("Vereinsheim am Leinecksee".equals(order.pickupPlace())) {
			textBuilder.append("Abholzeitraum: 09:00 - 12:00 Uhr<br>");
		} else if ("Weinstadt (Tanja & Ralph)".equals(order.pickupPlace())) {
			textBuilder.append("Abholzeitraum: 10:30 - 11:30 Uhr gegenüber von Cabriobad.<br>");
		}
		textBuilder.append("<br><br>")
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

	// Get the total number of smoked and fresh trout ordered
	public int getTotalQuantitySmoked() {
		return orderRepository.findAll().stream().mapToInt(Order::quantitySmoked).sum();
	}

	public int getTotalQuantityFresh() {
		return orderRepository.findAll().stream().mapToInt(Order::quantityFresh).sum();
	}
}