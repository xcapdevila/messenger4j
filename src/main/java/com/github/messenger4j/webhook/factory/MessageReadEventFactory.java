package com.github.messenger4j.webhook.factory;

import com.github.messenger4j.webhook.event.BaseEventType;
import com.github.messenger4j.webhook.event.MessageReadEvent;
import com.google.gson.JsonObject;

import java.time.Instant;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.*;
import static com.github.messenger4j.internal.gson.GsonUtil.*;

/**
 * @author Max Grabenhorst
 * @since 1.0.0
 */
final class MessageReadEventFactory implements BaseEventFactory<MessageReadEvent> {

	@Override
	public boolean isResponsible(JsonObject messagingEvent) {
		return hasProperty(messagingEvent, PROP_READ);
	}

	@Override
	public MessageReadEvent createEventFromJson(JsonObject messagingEvent, BaseEventType baseEventType) {
		final String senderId = getPropertyAsString(messagingEvent, PROP_SENDER, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final String recipientId = getPropertyAsString(messagingEvent, PROP_RECIPIENT, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final Instant timestamp = getPropertyAsInstant(messagingEvent, PROP_TIMESTAMP).orElseThrow(IllegalArgumentException::new);
		final Instant watermark = getPropertyAsInstant(messagingEvent, PROP_READ, PROP_WATERMARK).orElseThrow(IllegalArgumentException::new);

		return new MessageReadEvent(senderId, recipientId, timestamp, baseEventType, watermark);
	}
}
