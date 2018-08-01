package com.github.messenger4j.handover;

import com.google.gson.JsonObject;

import java.util.Optional;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
public final class ThreadOwnerResponseFactory {

	private ThreadOwnerResponseFactory() {
	}

	public static ThreadOwnerResponse create(JsonObject jsonObject) {
		//FIXME reveiw how to extract list + map
		return new ThreadOwnerResponse(Optional.empty());
	}
}
