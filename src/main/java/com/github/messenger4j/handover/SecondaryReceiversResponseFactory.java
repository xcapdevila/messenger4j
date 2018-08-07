package com.github.messenger4j.handover;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.*;
import static com.github.messenger4j.internal.gson.GsonUtil.getPropertyAsJsonArray;
import static com.github.messenger4j.internal.gson.GsonUtil.getPropertyAsString;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
public final class SecondaryReceiversResponseFactory {

	private SecondaryReceiversResponseFactory() {
	}

	public static SecondaryReceiversResponse create(JsonObject jsonObject) {
		List<SecondaryReceiver> secondaryReceivers = new ArrayList<>();
		JsonArray data = getPropertyAsJsonArray(jsonObject, PROP_DATA).orElse(new JsonArray());
		for (JsonElement dataArrayObject : data) {
			Optional<String> id = getPropertyAsString(dataArrayObject.getAsJsonObject(), PROP_ID);
			Optional<String> name = getPropertyAsString(dataArrayObject.getAsJsonObject(), PROP_NAME);
			secondaryReceivers.add(new SecondaryReceiver(id, name));
		}
		return new SecondaryReceiversResponse(Optional.of(secondaryReceivers));
	}

}
