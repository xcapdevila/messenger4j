package com.github.messenger4j.handover;

import com.github.messenger4j.send.recipient.IdRecipient;
import com.github.messenger4j.send.recipient.Recipient;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.Optional;

/**
 * PASS CONTROL
 *
 * @author Xavier Capdevila
 * @since 1.1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class HandoverPassThradControlPayload extends HandoverPayload {

	private final String targetAppId;

	public static HandoverPassThradControlPayload create(@NonNull String recipientId, @NonNull String targetAppId, @NonNull Optional<String> metadata) {
		return new HandoverPassThradControlPayload(IdRecipient.create(recipientId), targetAppId, metadata);
	}

	public HandoverPassThradControlPayload(Recipient recipient, String targetAppId, Optional<String> metadata) {
		super(recipient, metadata);
		this.targetAppId = targetAppId;
	}

	public String targetAppId() {
		return targetAppId;
	}

}
