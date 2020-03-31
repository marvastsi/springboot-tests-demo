package com.marvastsi.demo.auth.role;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

	ROLE_USER;

	@Override
	public String getAuthority() {
		return name();
	}
}
