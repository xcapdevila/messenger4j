package com.github.messenger4j.webhook.factory;

import com.github.messenger4j.webhook.event.BaseEventType;
import com.github.messenger4j.webhook.event.RequestThreadControlEvent;
import com.github.messenger4j.webhook.event.common.RequestThreadControl;
import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.Optional;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.*;
import static com.github.messenger4j.internal.gson.GsonUtil.*;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
final class RequestThreadControlEventFactory implements BaseEventFactory<RequestThreadControlEvent> {

	@Override
	public boolean isResponsible(JsonObject messagingEvent) {
		return hasProperty(messagingEvent, PROP_REQUEST_THREAD_CONTROL);
	}

	@Override
	public RequestThreadControlEvent createEventFromJson(JsonObject messagingEvent, BaseEventType baseEventType) {
		final String senderId = getPropertyAsString(messagingEvent, PROP_SENDER, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final String recipientId = getPropertyAsString(messagingEvent, PROP_RECIPIENT, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final Instant timestamp = getPropertyAsInstant(messagingEvent, PROP_TIMESTAMP).orElseThrow(IllegalArgumentException::new);
		final Optional<RequestThreadControl> requestThreadControl = getPropertyAsJsonObject(messagingEvent, PROP_REQUEST_THREAD_CONTROL)
				.map(this::getRequestThreadControlFromJsonObject);

		return new RequestThreadControlEvent(senderId, recipientId, timestamp, baseEventType, requestThreadControl);
	}
}
