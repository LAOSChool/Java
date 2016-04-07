package com.itpro.restws.security;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

/** Contains information about a token. */
public final class ApiKeyInfo {

	private final long created = System.currentTimeMillis();
	private final String token;
	private final UserDetails userDetails;
	// TODO expiration etc

	public ApiKeyInfo(String token, UserDetails userDetails) {
		this.token = token;
		this.userDetails = userDetails;
	}

	public String getToken() {
		return token;
	}

	@Override
	public String toString() {
		return "TokenInfo{" +
			"token='" + token + '\'' +
			", userDetails" + userDetails +
			", created=" + new Date(created) +
			'}';
	}
}
