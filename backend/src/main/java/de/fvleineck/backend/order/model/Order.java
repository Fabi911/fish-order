package de.fvleineck.backend.order.model;

import org.springframework.data.mongodb.core.mapping.MongoId;

public record Order(
		@MongoId
		String id,
		String lastname,
		String firstname,
		String email,
		int quantity_smoked,
		int quantity_fresh
) {
}