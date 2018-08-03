package com.github.messenger4j.handover;

import com.google.gson.JsonObject;

import java.util.Optional;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.PROP_SUCCESS;
import static com.github.messenger4j.internal.gson.GsonUtil.getPropertyAsBoolean;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
public final class HandoverResponseFactory {

	private HandoverResponseFactory() {
	}

	public static HandoverResponse create(JsonObject jsonObject) {
		final Optional<Boolean> success = getPropertyAsBoolean(jsonObject, PROP_SUCCESS);
		return new HandoverResponse(success);
	}
}
