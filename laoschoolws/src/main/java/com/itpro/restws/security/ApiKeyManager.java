package com.itpro.restws.security;

import com.itpro.restws.model.ApiKey;

/**
 * Manages api key - separated from {@link AuthenticationService},
 * so we can implement and plug various policies.
 */
public interface ApiKeyManager {

	/**
	 * Creates a new token for the user and returns its {@link TokenInfo}.
	 * It may add it to the token list or replace the previous one for the user. Never returns {@code null}.
	 */
	ApiKey createNewApiKey(String apiKey);
	void removeApiKey(String apiKey);
	ApiKey getApiKey(String apiKey);
}
