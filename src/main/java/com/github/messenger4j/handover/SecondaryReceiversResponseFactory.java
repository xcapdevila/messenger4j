package com.github.messenger4j.handover;

import com.google.gson.JsonObject;

import java.util.Optional;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
public final class SecondaryReceiversResponseFactory {

	private SecondaryReceiversResponseFactory() {
	}

	public static SecondaryReceiversResponse create(JsonObject jsonObject) {
		//FIXME reveiw how to extract list + map
		return new SecondaryReceiversResponse(Optional.empty());
	}
}
