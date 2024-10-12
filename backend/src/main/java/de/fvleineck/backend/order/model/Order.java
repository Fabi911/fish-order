package de.fvleineck.backend.order.model;


public record Order(
		String id,
		String lastname,
		String firstname,
		String email,
		String pickupPlace,
		String comment,
		int quantitySmoked,
		int quantityFresh
) {
}