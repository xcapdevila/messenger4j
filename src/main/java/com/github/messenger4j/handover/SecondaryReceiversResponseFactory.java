package com.github.messenger4j.handover;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.*;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
public final class SecondaryReceiversResponseFactory {

	private SecondaryReceiversResponseFactory() {
	}

	public static SecondaryReceiversResponse create(JsonObject jsonObject) {
		List<SecondaryReceiver> secondaryReceivers = new ArrayList<>();
		JsonArray data = jsonObject.getAsJsonArray(PROP_DATA.name());
		for (JsonElement dataArrayObject : data) {
			final JsonObject dataJsonObject = dataArrayObject.getAsJsonObject();
			String id = dataJsonObject.get(PROP_ID.name()).getAsString();
			String name = dataJsonObject.get(PROP_NAME.name()).getAsString();
			secondaryReceivers.add(new SecondaryReceiver(Optional.of(id), Optional.of(name)));
		}
		return new SecondaryReceiversResponse(Optional.of(secondaryReceivers));
	}

}
