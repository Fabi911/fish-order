package de.fvleineck.backend.order.model;


public record Order(
		String id,
		String lastname,
		String firstname,
		String email,
		String phone,
		String pickupPlace,
		String comment,
		Integer quantitySmoked,
		Integer quantityFresh
) {
}