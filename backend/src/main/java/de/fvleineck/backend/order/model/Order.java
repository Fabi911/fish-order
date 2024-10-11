package de.fvleineck.backend.order.model;

import org.springframework.data.mongodb.core.mapping.MongoId;

public record Order(
		@MongoId
		String id,
		String lastname,
		String firstname,
		String email,
		int quantitySmoked,
		int quantityFresh
) {
}