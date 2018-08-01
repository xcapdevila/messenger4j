package com.github.messenger4j.webhook.factory;

import com.github.messenger4j.internal.Lists;
import com.github.messenger4j.webhook.Event;
import com.github.messenger4j.webhook.event.BaseEventType;
import com.github.messenger4j.webhook.event.FallbackEvent;
import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.List;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.*;
import static com.github.messenger4j.internal.gson.GsonUtil.getPropertyAsInstant;
import static com.github.messenger4j.internal.gson.GsonUtil.getPropertyAsString;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
public final class EventFactory {

	private static final List<BaseEventFactory> FACTORIES = Lists//
			.immutableList(//
					new TextMessageEventFactory(),//
					new AttachmentMessageEventFactory(),//
					new QuickReplyMessageEventFactory(),//
					new PostbackEventFactory(),//
					new ReferralEventFactory(),//
					new OptInEventFactory(),//
					new MessageEchoEventFactory(),//
					new MessageDeliveredEventFactory(),//
					new MessageReadEventFactory(),//
					new AccountLinkingEventFactory(),//
					new InstantGameEventFactory(),//
					new PassThreadControlEventFactory(),//
					new TakeThreadControlEventFactory(),//
					new RequestThreadControlEventFactory(),//
					new AppRolesEventFactory()//
			);

	private EventFactory() {
	}

	public static Event createEvent(JsonObject messagingEvent, BaseEventType baseEventType) {
		for (BaseEventFactory factory : FACTORIES) {
			if (factory.isResponsible(messagingEvent)) {
				return new Event(factory.createEventFromJson(messagingEvent, baseEventType));
			}
		}
		final String senderId = getPropertyAsString(messagingEvent, PROP_SENDER, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final String recipientId = getPropertyAsString(messagingEvent, PROP_RECIPIENT, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final Instant timestamp = getPropertyAsInstant(messagingEvent, PROP_TIMESTAMP).orElse(Instant.now());
		return new Event(new FallbackEvent(senderId, recipientId, timestamp, baseEventType));
	}
}
