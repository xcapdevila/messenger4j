package com.github.messenger4j.webhook.event;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;

/**
 * @author Max Grabenhorst
 * @since 1.0.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class MessageReadEvent extends BaseEvent {

	private final Instant watermark;

	public MessageReadEvent(@NonNull String senderId, @NonNull String recipientId, @NonNull Instant timestamp, @NonNull BaseEventType baseEventType,
			@NonNull Instant watermark) {
		super(senderId, recipientId, timestamp, baseEventType);
		this.watermark = watermark;
	}

	public Instant watermark() {
		return watermark;
	}
}
