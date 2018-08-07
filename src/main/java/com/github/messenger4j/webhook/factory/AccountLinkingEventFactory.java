package com.github.messenger4j.webhook.factory;

import com.github.messenger4j.webhook.event.AccountLinkingEvent;
import com.github.messenger4j.webhook.event.BaseEventType;
import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.Optional;

import static com.github.messenger4j.internal.gson.GsonUtil.Constants.*;
import static com.github.messenger4j.internal.gson.GsonUtil.*;

/**
 * @author Max Grabenhorst
 * @since 1.0.0
 */
final class AccountLinkingEventFactory implements BaseEventFactory<AccountLinkingEvent> {

	@Override
	public boolean isResponsible(JsonObject messagingEvent) {
		return hasProperty(messagingEvent, PROP_ACCOUNT_LINKING);
	}

	@Override
	public AccountLinkingEvent createEventFromJson(JsonObject messagingEvent, BaseEventType baseEventType) {
		final String senderId = getPropertyAsString(messagingEvent, PROP_SENDER, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final String recipientId = getPropertyAsString(messagingEvent, PROP_RECIPIENT, PROP_ID).orElseThrow(IllegalArgumentException::new);
		final Instant timestamp = getPropertyAsInstant(messagingEvent, PROP_TIMESTAMP).orElseThrow(IllegalArgumentException::new);
		final AccountLinkingEvent.Status status = getPropertyAsString(messagingEvent, PROP_ACCOUNT_LINKING, PROP_STATUS).map(String::toUpperCase)
				.map(AccountLinkingEvent.Status::valueOf).orElseThrow(IllegalArgumentException::new);
		final Optional<String> authorizationCode = getPropertyAsString(messagingEvent, PROP_ACCOUNT_LINKING, PROP_AUTHORIZATION_CODE);

		return new AccountLinkingEvent(senderId, recipientId, timestamp, baseEventType, status, authorizationCode);
	}
}
