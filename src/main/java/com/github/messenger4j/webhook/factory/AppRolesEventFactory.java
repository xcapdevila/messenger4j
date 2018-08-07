package com.github.messenger4j.webhook.factory;

import com.github.messenger4j.webhook.event.AppRolesEvent;
import com.github.messenger4j.webhook.event.BaseEventType;
import com.github.messenger4j.webhook.event.common.AppRoles;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.*;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.*;
import static com.github.messenger4j.internal.gson.GsonUtil.*;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
final class AppRolesEventFactory implements BaseEventFactory<AppRolesEvent> {

	@Override
	public boolean isResponsible(JsonObject messagingEvent) {
		return hasProperty(messagingEvent, PROP_APP_ROLES);
	}

	@Override
	public AppRolesEvent createEventFromJson(JsonObject messagingEvent, BaseEventType baseEventType) {
		final String recipientId = getPropertyAsString(messagingEvent, PROP_RECIPIENT, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final Instant timestamp = getPropertyAsInstant(messagingEvent, PROP_TIMESTAMP).orElseThrow(IllegalArgumentException::new);
		final Optional<AppRoles> appRoles = getPropertyAsJsonObject(messagingEvent, PROP_APP_ROLES).map(this::getIdRolesFromJsonObject);

		return new AppRolesEvent(recipientId, timestamp, baseEventType, appRoles);
	}

	private AppRoles getIdRolesFromJsonObject(JsonObject jsonObject) {
		final Map<String, List<String>> idRoles = new HashMap<>();
		for (String key : jsonObject.keySet()) {
			final JsonArray valuesJsonArray = jsonObject.getAsJsonArray(key);
			final List<String> values = new ArrayList<>(valuesJsonArray.size());
			for (JsonElement jsonElement : valuesJsonArray) {
				values.add(jsonElement.getAsString());
			}
			idRoles.put(key, Collections.unmodifiableList(values));
		}
		return new AppRoles(idRoles);
	}
}
