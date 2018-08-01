package com.github.messenger4j.webhook.factory;

import com.github.messenger4j.webhook.event.BaseEventType;
import com.github.messenger4j.webhook.event.OptInEvent;
import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.Optional;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.*;
import static com.github.messenger4j.internal.gson.GsonUtil.*;

/**
 * @author Max Grabenhorst
 * @since 1.0.0
 */
final class OptInEventFactory implements BaseEventFactory<OptInEvent> {

	@Override
	public boolean isResponsible(JsonObject messagingEvent) {
		return hasProperty(messagingEvent, PROP_OPTIN);
	}

	@Override
	public OptInEvent createEventFromJson(JsonObject messagingEvent, BaseEventType baseEventType) {
		final Optional<String> senderId = getPropertyAsString(messagingEvent, PROP_SENDER, PROP_ID);
		final String recipientId = getPropertyAsString(messagingEvent, PROP_RECIPIENT, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final Instant timestamp = getPropertyAsInstant(messagingEvent, PROP_TIMESTAMP).orElseThrow(IllegalArgumentException::new);
		final Optional<String> refPayload = getPropertyAsString(messagingEvent, PROP_OPTIN, PROP_REF);
		final Optional<String> userRefPayload = getPropertyAsString(messagingEvent, PROP_OPTIN, PROP_USER_REF);

		return new OptInEvent(senderId, recipientId, timestamp, baseEventType, refPayload, userRefPayload);
	}
}
