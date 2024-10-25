package de.fvleineck.backend.security.models;

import de.webdev.backend.security.AppuserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "users")
public class AppUser {
	@MongoId
	private String id;
	private String username;
	private String password;
	private AppuserRole role;
}