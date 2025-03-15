package de.fvleineck.backend.security.repository;

import de.fvleineck.backend.security.models.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AppuserRepository extends MongoRepository<AppUser, String> {
	Optional<AppUser> findByUsername(String username);
}