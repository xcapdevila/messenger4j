package com.github.messenger4j.handover;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.*;
import static com.github.messenger4j.internal.gson.GsonUtil.*;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
public final class ThreadOwnerResponseFactory {

	private ThreadOwnerResponseFactory() {
	}

	public static ThreadOwnerResponse create(JsonObject jsonObject) {
		ThreadOwner threadOwner = null;
		JsonArray data = getPropertyAsJsonArray(jsonObject, PROP_DATA).orElse(new JsonArray());
		for (JsonElement dataArrayObject : data) {
			JsonObject threadOwnerJsonObject = getPropertyAsJsonObject(dataArrayObject.getAsJsonObject(), PROP_THREAD_OWNER).orElse(new JsonObject());
			Optional<String> appId = getPropertyAsString(threadOwnerJsonObject, PROP_APP_ID);
			threadOwner = new ThreadOwner(appId);
		}
		return new ThreadOwnerResponse(Optional.ofNullable(threadOwner));
	}

}
