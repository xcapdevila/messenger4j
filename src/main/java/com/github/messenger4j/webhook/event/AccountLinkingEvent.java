package com.github.messenger4j.webhook.event;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;
import java.util.Optional;

/**
 * @author Max Grabenhorst
 * @since 1.0.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class AccountLinkingEvent extends BaseEvent {

	private final Status status;
	private final Optional<String> authorizationCode;

	public AccountLinkingEvent(@NonNull String senderId, @NonNull String recipientId, @NonNull Instant timestamp, @NonNull BaseEventType baseEventType,
			@NonNull Status status, @NonNull Optional<String> authorizationCode) {
		super(senderId, recipientId, timestamp, baseEventType);
		this.status = status;
		this.authorizationCode = authorizationCode;
	}

	public Status status() {
		return status;
	}

	public Optional<String> authorizationCode() {
		return authorizationCode;
	}

	/**
	 * @since 1.0.0
	 */
	public enum Status {
		LINKED,
		UNLINKED
	}
}
