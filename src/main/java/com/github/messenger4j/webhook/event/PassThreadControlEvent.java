package com.github.messenger4j.webhook.event;

import com.github.messenger4j.webhook.event.common.PassThreadControl;
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
public final class PassThreadControlEvent extends BaseEvent {

	private final Optional<PassThreadControl> passThreadControl;

	public PassThreadControlEvent(@NonNull String senderId, @NonNull String recipientId, @NonNull Instant timestamp, @NonNull BaseEventType baseEventType,
			@NonNull Optional<PassThreadControl> passThreadControl) {
		super(senderId, recipientId, timestamp, baseEventType);
		this.passThreadControl = passThreadControl;
	}

	public Optional<PassThreadControl> getPassThreadControl() {
		return passThreadControl;
	}
}
