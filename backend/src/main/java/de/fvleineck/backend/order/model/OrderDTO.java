package de.fvleineck.backend.order.model;

public record OrderDTO(
		String lastname,
		String firstname,
		String email,
		int quantitySmoked,
		int quantityFresh
) {
}