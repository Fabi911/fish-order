package de.fvleineck.backend.security.models;

import de.fvleineck.backend.security.AppuserRole;

public record AppUserResponse(
		String id,
		String username,
		AppuserRole role
) {
}