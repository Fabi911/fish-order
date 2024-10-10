package de.fvleineck.backend.order.repository;

import de.fvleineck.backend.order.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}