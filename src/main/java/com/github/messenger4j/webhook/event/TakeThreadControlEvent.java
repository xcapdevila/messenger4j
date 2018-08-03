package com.github.messenger4j.webhook.event;

import com.github.messenger4j.webhook.event.common.TakeThreadControl;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;
import java.util.Optional;

/**
 * @author Xavier Capdevila
 * @since 1.1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class TakeThreadControlEvent extends BaseEvent {

	private final Optional<TakeThreadControl> takeThreadControl;

	public TakeThreadControlEvent(@NonNull String senderId, @NonNull String recipientId, @NonNull Instant timestamp, @NonNull BaseEventType baseEventType,
			@NonNull Optional<TakeThreadControl> takeThreadControl) {
		super(senderId, recipientId, timestamp, baseEventType);
		this.takeThreadControl = takeThreadControl;
	}

	public Optional<TakeThreadControl> getTakeThreadControl() {
		return takeThreadControl;
	}
}
