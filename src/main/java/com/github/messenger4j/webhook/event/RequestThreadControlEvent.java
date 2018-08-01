package com.github.messenger4j.webhook.event;

import com.github.messenger4j.webhook.event.common.RequestThreadControl;
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
public final class RequestThreadControlEvent extends BaseEvent {

	private final Optional<RequestThreadControl> requestThreadControl;

	public RequestThreadControlEvent(@NonNull String senderId, @NonNull String recipientId, @NonNull Instant timestamp, @NonNull BaseEventType baseEventType,
			@NonNull Optional<RequestThreadControl> requestThreadControl) {
		super(senderId, recipientId, timestamp, baseEventType);
		this.requestThreadControl = requestThreadControl;
	}

	public Optional<RequestThreadControl> getRequestThreadControl() {
		return requestThreadControl;
	}
}
