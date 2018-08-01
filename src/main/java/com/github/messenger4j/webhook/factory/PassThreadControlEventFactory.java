package com.github.messenger4j.webhook.factory;

import com.github.messenger4j.webhook.event.BaseEventType;
import com.github.messenger4j.webhook.event.PassThreadControlEvent;
import com.github.messenger4j.webhook.event.common.PassThreadControl;
import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.Optional;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.*;
import static com.github.messenger4j.internal.gson.GsonUtil.*;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
final class PassThreadControlEventFactory implements BaseEventFactory<PassThreadControlEvent> {

	@Override
	public boolean isResponsible(JsonObject messagingEvent) {
		return hasProperty(messagingEvent, PROP_PASS_THREAD_CONTROL);
	}

	@Override
	public PassThreadControlEvent createEventFromJson(JsonObject messagingEvent, BaseEventType baseEventType) {
		final String senderId = getPropertyAsString(messagingEvent, PROP_SENDER, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final String recipientId = getPropertyAsString(messagingEvent, PROP_RECIPIENT, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final Instant timestamp = getPropertyAsInstant(messagingEvent, PROP_TIMESTAMP).orElseThrow(IllegalArgumentException::new);
		final Optional<PassThreadControl> passThreadControl = getPropertyAsJsonObject(messagingEvent, PROP_PASS_THREAD_CONTROL)
				.map(this::getPassThreadControlFromJsonObject);

		return new PassThreadControlEvent(senderId, recipientId, timestamp, baseEventType, passThreadControl);
	}
}
