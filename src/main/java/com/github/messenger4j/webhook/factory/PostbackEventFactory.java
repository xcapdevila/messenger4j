package com.github.messenger4j.webhook.factory;

import com.github.messenger4j.webhook.event.BaseEventType;
import com.github.messenger4j.webhook.event.PostbackEvent;
import com.github.messenger4j.webhook.event.common.PriorMessage;
import com.github.messenger4j.webhook.event.common.Referral;
import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.Optional;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.*;
import static com.github.messenger4j.internal.gson.GsonUtil.*;

/**
 * @author Max Grabenhorst
 * @since 1.0.0
 */
final class PostbackEventFactory implements BaseEventFactory<PostbackEvent> {

	@Override
	public boolean isResponsible(JsonObject messagingEvent) {
		return hasProperty(messagingEvent, PROP_POSTBACK);
	}

	@Override
	public PostbackEvent createEventFromJson(JsonObject messagingEvent, BaseEventType baseEventType) {
		final String senderId = getPropertyAsString(messagingEvent, PROP_SENDER, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final String recipientId = getPropertyAsString(messagingEvent, PROP_RECIPIENT, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final Instant timestamp = getPropertyAsInstant(messagingEvent, PROP_TIMESTAMP).orElseThrow(IllegalArgumentException::new);
		final String title = getPropertyAsString(messagingEvent, PROP_POSTBACK, PROP_TITLE).orElseThrow(IllegalArgumentException::new);
		final Optional<String> payload = getPropertyAsString(messagingEvent, PROP_POSTBACK, PROP_PAYLOAD);
		final Optional<Referral> referral = getPropertyAsJsonObject(messagingEvent, PROP_POSTBACK, PROP_REFERRAL).map(this::createReferralFromJson);
		final Optional<PriorMessage> priorMessage = getPropertyAsJsonObject(messagingEvent, PROP_PRIOR_MESSAGE).map(this::getPriorMessageFromJsonObject);

		return new PostbackEvent(senderId, recipientId, timestamp, baseEventType, title, payload, referral, priorMessage);
	}
}
