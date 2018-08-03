package com.github.messenger4j.webhook.event;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

/**
 * @author Max Grabenhorst
 * @since 1.1.0
 */
@ToString
@EqualsAndHashCode
public abstract class BaseEvent {

	private final String senderId;
	private final String recipientId;
	private final Instant timestamp;
	private final BaseEventType baseEventType;

	BaseEvent(String senderId, String recipientId, Instant timestamp, BaseEventType baseEventType) {
		this.senderId = senderId;
		this.recipientId = recipientId;
		this.timestamp = timestamp;
		this.baseEventType = baseEventType;
	}

	public String senderId() {
		return senderId;
	}

	public String recipientId() {
		return recipientId;
	}

	public Instant timestamp() {
		return timestamp;
	}

	public BaseEventType baseEventType() {
		return baseEventType;
	}
}
