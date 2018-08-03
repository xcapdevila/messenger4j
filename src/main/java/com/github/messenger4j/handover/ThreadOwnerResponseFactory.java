package com.github.messenger4j.handover;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.PROP_APP_ID;
import static com.github.messenger4j.internal.gson.GsonUtil.Constants.PROP_DATA;
import static com.github.messenger4j.internal.gson.GsonUtil.Constants.PROP_THREAD_OWNER;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
public final class ThreadOwnerResponseFactory {

	private ThreadOwnerResponseFactory() {
	}

	public static ThreadOwnerResponse create(JsonObject jsonObject) {
		ThreadOwner threadOwner = null;
		JsonArray data = jsonObject.getAsJsonArray(PROP_DATA.name());
		for (JsonElement dataArrayObject : data) {
			final JsonObject dataJsonObject = dataArrayObject.getAsJsonObject();
			final JsonObject threadOwnerJsonObject = dataJsonObject.get(PROP_THREAD_OWNER.name()).getAsJsonObject();
			String appId = threadOwnerJsonObject.get(PROP_APP_ID.name()).getAsString();
			threadOwner = new ThreadOwner(Optional.of(appId));
		}
		return new ThreadOwnerResponse(Optional.ofNullable(threadOwner));
	}

}
